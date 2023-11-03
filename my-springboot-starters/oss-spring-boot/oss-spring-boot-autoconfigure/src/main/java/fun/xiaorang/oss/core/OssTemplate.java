package fun.xiaorang.oss.core;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import fun.xiaorang.oss.enums.PolicyType;
import fun.xiaorang.oss.properties.OssProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">参考自 <a href="https://docs.aws.amazon.com/zh_cn/sdk-for-java/v1/developer-guide/welcome.html">开发者指南-AWS SDK for Java 1.x</a> 以及 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/Welcome.html">用户指南</a><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/31 15:38
 */
@Slf4j
@RequiredArgsConstructor
public class OssTemplate {
    private final AmazonS3 s3Client;
    private final OssProperties ossProperties;

    /**
     * 如果默认的桶不存在，并且配置了自动创建，则在初始化时创建默认的桶，否则抛出异常
     */
    @PostConstruct
    public void init() {
        String bucketName = ossProperties.getBucketName();
        boolean existed = this.doesBucketExists(bucketName);
        if (!existed) {
            boolean createBucketIfNotExist = ossProperties.isCreateBucketIfNotExist();
            if (!createBucketIfNotExist) {
                throw new IllegalStateException(bucketName + "bucket does not exist!");
            }
            this.createBucket(bucketName);
        }
    }

    /**
     * 检查是否存在指定的桶
     *
     * @param bucketName 桶名称
     * @return true 存在，false 不存在
     */
    public boolean doesBucketExists(String bucketName) {
        return s3Client.doesBucketExistV2(bucketName);
    }

    /**
     * 根据指定的桶名称创建一个桶
     *
     * @param bucketName 桶名称
     * @param policyText 访问策略
     * @return 桶
     */
    public Bucket createBucket(String bucketName, String policyText) {
        if (doesBucketExists(bucketName)) {
            log.info("Bucket {} already exists.", bucketName);
            return getBucket(bucketName);
        }
        Bucket bucket = s3Client.createBucket(new CreateBucketRequest(bucketName));
        this.setBucketPolicy(bucketName, policyText);
        log.info("create bucket {} success!", bucketName);
        return bucket;
    }

    /**
     * 根据指定的桶名称创建一个桶
     *
     * @param bucketName 桶名称
     * @param policyType 访问策略类型
     * @return 桶
     */
    public Bucket createBucket(String bucketName, PolicyType policyType) {
        return this.createBucket(bucketName, policyType.getPolicy(bucketName));
    }

    /**
     * 根据指定的桶名称创建一个桶，使用配置的访问策略，默认为只读
     *
     * @param bucketName 桶名称
     * @return 桶
     */
    public Bucket createBucket(String bucketName) {
        return this.createBucket(bucketName, ossProperties.getPolicyType());
    }

    /**
     * 设置桶的访问策略
     *
     * @param bucketName 桶名称
     * @param policyText 访问策略
     */
    public void setBucketPolicy(String bucketName, String policyText) {
        s3Client.setBucketPolicy(bucketName, policyText);
    }

    /**
     * 设置桶的访问策略
     *
     * @param bucketName 桶名称
     * @param policyType 访问策略类型
     */
    public void setBucketPolicy(String bucketName, PolicyType policyType) {
        this.setBucketPolicy(bucketName, policyType.getPolicy(bucketName));
    }

    /**
     * 获取所有存储桶
     *
     * @return 存储桶列表
     */
    public List<Bucket> listBuckets() {
        return s3Client.listBuckets();
    }

    /**
     * 根据指定的桶名称获取桶
     *
     * @param bucketName 桶名称
     * @return 桶
     */
    public Bucket getBucket(String bucketName) {
        return listBuckets().stream()
                .filter(bucket -> bucket.getName().equals(bucketName))
                .findAny()
                .orElseThrow(() -> new IllegalStateException(bucketName + "bucket does not exist!"));
    }

    /**
     * 根据指定的桶名称删除桶，参考自 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/delete-bucket.html">删除桶</a>
     *
     * @param bucketName 桶名称
     * @return true 删除成功，false 删除失败
     */
    public boolean deleteBucket(String bucketName) {
        try {
            // Delete all objects from the bucket. This is sufficient
            // for unversioned buckets. For versioned buckets, when you attempt to delete objects, Amazon S3 inserts
            // delete markers for all objects, but doesn't delete the object versions.
            // To delete objects from versioned buckets, delete all of the object versions before deleting
            // the bucket (see below for an example).
            ObjectListing objectListing = s3Client.listObjects(bucketName);
            while (true) {
                for (S3ObjectSummary s3ObjectSummary : objectListing.getObjectSummaries()) {
                    s3Client.deleteObject(bucketName, s3ObjectSummary.getKey());
                }
                // If the bucket contains many objects, the listObjects() call
                // might not return all of the objects in the first listing. Check to
                // see whether the listing was truncated. If so, retrieve the next page of objects
                // and delete them.
                if (objectListing.isTruncated()) {
                    objectListing = s3Client.listNextBatchOfObjects(objectListing);
                } else {
                    break;
                }
            }

            // Delete all object versions (required for versioned buckets).
            VersionListing versionList = s3Client.listVersions(new ListVersionsRequest().withBucketName(bucketName));
            while (true) {
                for (S3VersionSummary vs : versionList.getVersionSummaries()) {
                    s3Client.deleteVersion(bucketName, vs.getKey(), vs.getVersionId());
                }
                if (versionList.isTruncated()) {
                    versionList = s3Client.listNextBatchOfVersions(versionList);
                } else {
                    break;
                }
            }

            // After all objects and object versions are deleted, delete the bucket.
            s3Client.deleteBucket(bucketName);
            log.info("delete bucket {} success!", bucketName);
            return true;
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            log.error("delete bucket error!", e);
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client couldn't
            // parse the response from Amazon S3.
            log.error("delete bucket error!", e);
        }
        return false;
    }

    /**
     * 获取文件外链，参考自 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/ShareObjectPreSignedURL.html">使用预签名 URL 共享对象</a>
     *
     * @param bucketName        桶名称
     * @param objectKey         对象名称
     * @param expires           过期时间，请注意该值必须小于7天
     * @param httpMethod        文件操作方法：GET（下载）、PUT（上传）
     * @param requestParameters 请求参数
     * @return 预签名的 URL
     */
    public URL getObjectUrl(String bucketName, String objectKey, Duration expires, HttpMethod httpMethod, Map<String, String> requestParameters) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(httpMethod)
                .withExpiration(Date.from(Instant.now().plus(expires)));
        if (requestParameters != null) {
            requestParameters.forEach(generatePresignedUrlRequest::addRequestParameter);
        }
        return s3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }

    /**
     * 获取文件外链
     *
     * @param bucketName        桶名称
     * @param objectKey         对象名称
     * @param minutes           过期时间，请注意该值必须小于7天，单位：分钟
     * @param httpMethod        文件操作方法：GET（下载）、PUT（上传）
     * @param requestParameters 请求参数
     * @return 预签名的 URL
     */
    public URL getObjectUrl(String bucketName, String objectKey, int minutes, HttpMethod httpMethod, Map<String, String> requestParameters) {
        if (minutes > 7 * 24 * 60 || minutes <= 0) {
            throw new IllegalArgumentException("minutes must be less than 7 days and greater than 0");
        }
        return getObjectUrl(bucketName, objectKey, Duration.ofMinutes(minutes), httpMethod, requestParameters);
    }

    /**
     * 获取文件外链，只用于下载
     *
     * @param bucketName 桶名称
     * @param objectKey  对象名称
     * @param minutes    过期时间，请注意该值必须小于7天，单位：分钟
     * @return 预签名的 URL
     */
    public String getObjectUrl(String bucketName, String objectKey, int minutes) {
        boolean existed = s3Client.doesObjectExist(bucketName, objectKey);
        if (!existed) {
            throw new IllegalStateException("object " + objectKey + " does not exist in " + bucketName + " bucket!");
        }
        return getObjectUrl(bucketName, objectKey, minutes, HttpMethod.GET, null).toString();
    }

    /**
     * 获取文件外链，只用于下载，默认过期时间为 10 分钟
     *
     * @param bucketName 桶名称
     * @param objectKey  对象名称
     * @return 预签名的 URL
     */
    public String getObjectUrl(String bucketName, String objectKey) {
        return getObjectUrl(bucketName, objectKey, ossProperties.getUrlExpirationTime());
    }

    /**
     * 从默认的桶中获取文件外链，只用于下载，默认过期时间为 10 分钟
     *
     * @param objectKey 对象名称
     * @return 预签名的 URL
     */
    public String getObjectUrl(String objectKey) {
        return getObjectUrl(ossProperties.getBucketName(), objectKey);
    }

    /**
     * 获取文件上传外链，只用于上传
     *
     * @param bucketName        桶名称
     * @param objectKey         对象名称
     * @param minutes           过期时间，请注意该值必须小于7天，单位：分钟
     * @param requestParameters 请求参数
     * @return 预签名的 URL
     */
    public URL getPutObjectUrl(String bucketName, String objectKey, int minutes, Map<String, String> requestParameters) {
        return getObjectUrl(bucketName, objectKey, minutes, HttpMethod.PUT, requestParameters);
    }

    /**
     * 获取文件上传外链，只用于上传，默认过期时间为 10 分钟
     *
     * @param bucketName        桶名称
     * @param objectKey         对象名称
     * @param requestParameters 请求参数
     * @return 预签名的 URL
     */
    public URL getPutObjectUrl(String bucketName, String objectKey, Map<String, String> requestParameters) {
        return getPutObjectUrl(bucketName, objectKey, ossProperties.getUrlExpirationTime(), requestParameters);
    }

    /**
     * 从默认的桶中获取文件上传外链，只用于上传，默认过期时间为 10 分钟
     *
     * @param objectKey 对象名称
     * @return 预签名的 URL
     */
    public URL getPutObjectUrl(String objectKey) {
        return getPutObjectUrl(ossProperties.getBucketName(), objectKey, null);
    }

    /**
     * 上传文件到指定的桶中，参考自 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/upload-objects.html">上传对象</a>
     *
     * @param bucketName  桶名称
     * @param objectKey   文件名称，例如：test/1.txt
     * @param inputStream 要上传文件的输入流
     * @param contextType 文件类型
     * @return 上传结果
     */
    public PutObjectResult putObject(String bucketName, String objectKey, InputStream inputStream, int size, String contextType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contextType);
        objectMetadata.setContentLength(size);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, inputStream, objectMetadata);
        PutObjectResult putObjectResult = s3Client.putObject(putObjectRequest);
        // Setting the read limit value to one byte greater than the size of stream will reliably avoid a ResetException
        putObjectRequest.getRequestClientOptions().setReadLimit(size + 1);
        log.info("put object success! bucketName:{}, objectKey:{}", bucketName, objectKey);
        return putObjectResult;
    }

    /**
     * 上传文件到指定的桶中
     *
     * @param bucketName  桶名称
     * @param objectKey   文件名称，例如：test/1.txt
     * @param inputStream 要上传文件的输入流
     * @param contextType 文件类型
     * @return 上传结果
     * @throws IOException IO异常
     */
    public PutObjectResult putObject(String bucketName, String objectKey, InputStream inputStream, String contextType) throws IOException {
        return putObject(bucketName, objectKey, inputStream, inputStream.available(), contextType);
    }

    /**
     * 上传文件到指定的桶中
     *
     * @param bucketName  桶名称
     * @param objectKey   文件名称，例如：test/1.txt
     * @param inputStream 要上传文件的输入流
     * @return 上传结果
     * @throws IOException IO异常
     */
    public PutObjectResult putObject(String bucketName, String objectKey, InputStream inputStream) throws IOException {
        return putObject(bucketName, objectKey, inputStream, "application/octet-stream");
    }

    /**
     * 上传文件到默认的桶中
     *
     * @param objectKey   文件名称，例如：test/1.txt
     * @param inputStream 要上传文件的输入流
     * @return 上传结果
     * @throws IOException IO异常
     */
    public PutObjectResult putObject(String objectKey, InputStream inputStream) throws IOException {
        return putObject(ossProperties.getBucketName(), objectKey, inputStream);
    }

    /**
     * 判断指定的桶中是否存在指定的文件
     *
     * @param bucketName 桶名称
     * @param objectKey  对象名称
     * @return true 存在，false 不存在
     */
    public boolean doesObjectExist(String bucketName, String objectKey) {
        return s3Client.doesObjectExist(bucketName, objectKey);
    }

    /**
     * 判断默认的桶中是否存在指定的文件
     *
     * @param objectKey 对象名称
     * @return true 存在，false 不存在
     */
    public boolean doesObjectExist(String objectKey) {
        return this.doesObjectExist(ossProperties.getBucketName(), objectKey);
    }

    /**
     * 从指定的桶中获取文件
     *
     * @param bucketName 桶名称
     * @param objectKey  对象名称
     * @return 文件
     */
    public S3Object getObject(String bucketName, String objectKey) {
        boolean existed = this.doesObjectExist(bucketName, objectKey);
        if (!existed) {
            throw new IllegalStateException("object " + objectKey + " does not exist in " + bucketName + " bucket!");
        }
        return s3Client.getObject(bucketName, objectKey);
    }

    /**
     * 从默认的桶中获取文件
     *
     * @param objectKey 对象名称
     * @return 文件的输入流
     */
    public S3Object getObject(String objectKey) {
        return this.getObject(ossProperties.getBucketName(), objectKey);
    }

    /**
     * 从指定的桶中删除文件
     *
     * @param bucketName 桶名称
     * @param fileName   文件名称
     */
    public void deleteObject(String bucketName, String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        log.info("delete object success! bucketName:{}, fileName:{}", bucketName, fileName);
    }

    /**
     * 从默认的桶中删除文件
     *
     * @param fileName 文件名称
     */
    public void deleteObject(String fileName) {
        this.deleteObject(ossProperties.getBucketName(), fileName);
    }

    /**
     * 初始化分片上传，参考自 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/mpuoverview.html#mpu-process">使用分段上传来上传和复制对象-分段上传流程-分段上传开始</a> 以及 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/mpu-upload-object.html">使用分段上传上传对象</a>
     * 返回具有上传 ID 的响应，此 ID 是分段上传的唯一标识符。无论您何时上传分段、列出分段、完成上传或停止上传，您都必须包括此上传 ID。
     *
     * @param bucketName  桶名称
     * @param objectKey   对象名称
     * @param contentType 文件类型
     * @return 初始化分片上传结果
     */
    public InitiateMultipartUploadResult initiateMultipartUpload(String bucketName, String objectKey, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName, objectKey, objectMetadata);
        return s3Client.initiateMultipartUpload(initiateMultipartUploadRequest);
    }

    /**
     * 初始化分片上传
     *
     * @param objectKey   对象名称
     * @param contentType 文件类型
     * @return 初始化分片上传结果
     */
    public InitiateMultipartUploadResult initiateMultipartUpload(String objectKey, String contentType) {
        return this.initiateMultipartUpload(ossProperties.getBucketName(), objectKey, contentType);
    }


    /**
     * 初始化分片上传
     *
     * @param objectKey 对象名称
     * @return 初始化分片上传结果
     */
    public InitiateMultipartUploadResult initiateMultipartUpload(String objectKey) {
        return this.initiateMultipartUpload(ossProperties.getBucketName(), objectKey, "application/octet-stream");
    }

    /**
     * 上传分片
     *
     * @param uploadId    上传 ID，需要先调用 {@link #initiateMultipartUpload(String, String)} 方法获取
     * @param bucketName  桶名称
     * @param objectKey   对象名称
     * @param partNumber  分片编号
     * @param partSize    分片大小，单位：字节，最小为 5MB，最大为 5GB，未对最后一个分片大小做限制；例如，分片大小为 5MB，文件大小为 16MB，则分为 4 个分片，分片编号分别为 1、2、3、4，最后一个分片大小为 1MB；参考自 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/qfacts.html">Amazon S3 分段上传限制</a>
     * @param inputStream 分片的输入流
     * @return 上传分片结果
     */
    public UploadPartResult uploadPart(String uploadId, String bucketName, String objectKey, int partNumber, long partSize, InputStream inputStream) {
        UploadPartRequest uploadPartRequest = new UploadPartRequest()
                .withUploadId(uploadId)
                .withBucketName(bucketName)
                .withKey(objectKey)
                .withPartNumber(partNumber)
                .withPartSize(partSize)
                .withInputStream(inputStream);
        return s3Client.uploadPart(uploadPartRequest);
    }

    /**
     * 上传分片
     *
     * @param uploadId    上传 ID，需要先调用 {@link #initiateMultipartUpload(String, String)} 方法获取
     * @param objectKey   对象名称
     * @param partNumber  分片编号
     * @param partSize    分片大小，单位：字节，最小为 5MB，最大为 5GB，未对最后一个分片大小做限制；例如，分片大小为 5MB，文件大小为 16MB，则分为 4 个分片，分片编号分别为 1、2、3、4，最后一个分片大小为 1MB；参考自 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/qfacts.html">Amazon S3 分段上传限制</a>
     * @param inputStream 分片的输入流
     * @return 上传分片结果
     */
    public UploadPartResult uploadPart(String uploadId, String objectKey, int partNumber, long partSize, InputStream inputStream) {
        return this.uploadPart(uploadId, ossProperties.getBucketName(), objectKey, partNumber, partSize, inputStream);
    }

    /**
     * 列出已上传完成的分片，参考自 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/list-mpu.html">列出分段上传</a>
     *
     * @param uploadId   上传 ID，需要先调用 {@link #initiateMultipartUpload(String, String)} 方法获取
     * @param bucketName 桶名称
     * @param objectKey  对象名称
     * @return 已上传完成的分片
     */
    public PartListing listParts(String uploadId, String bucketName, String objectKey) {
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, objectKey, uploadId);
        return s3Client.listParts(listPartsRequest);
    }

    /**
     * 列出已上传完成的分片
     *
     * @param uploadId  上传 ID，需要先调用 {@link #initiateMultipartUpload(String, String)} 方法获取
     * @param objectKey 对象名称
     * @return 已上传完成的分片
     */
    public PartListing listParts(String uploadId, String objectKey) {
        return this.listParts(uploadId, ossProperties.getBucketName(), objectKey);
    }

    /**
     * 合并分片，参考自 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/mpuoverview.html#mpu-process">使用分段上传来上传和复制对象-分段上传流程-分段上传完成</a>
     *
     * @param uploadId      上传 ID，需要先调用 {@link #initiateMultipartUpload(String, String)} 方法获取
     * @param bucketName    桶名称
     * @param objectKey     对象名称
     * @param partSummaries 分片列表
     * @return 合并分片结果
     */
    public CompleteMultipartUploadResult completeMultipartUpload(String uploadId, String bucketName, String objectKey, List<PartSummary> partSummaries) {
        List<PartETag> partETags = partSummaries.stream()
                .map(partSummary -> new PartETag(partSummary.getPartNumber(), partSummary.getETag()))
                .collect(Collectors.toList());
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, objectKey, uploadId, partETags);
        return s3Client.completeMultipartUpload(completeMultipartUploadRequest);
    }

    /**
     * 合并分片
     *
     * @param uploadId   上传 ID，需要先调用 {@link #initiateMultipartUpload(String, String)} 方法获取
     * @param bucketName 桶名称
     * @param objectKey  对象名称
     * @return 合并分片结果
     */
    public CompleteMultipartUploadResult completeMultipartUpload(String uploadId, String bucketName, String objectKey) {
        PartListing partListing = this.listParts(uploadId, bucketName, objectKey);
        return this.completeMultipartUpload(uploadId, bucketName, objectKey, partListing.getParts());
    }

    /**
     * 合并分片
     *
     * @param uploadId  上传 ID，需要先调用 {@link #initiateMultipartUpload(String, String)} 方法获取
     * @param objectKey 对象名称
     * @return 合并分片结果
     */
    public CompleteMultipartUploadResult completeMultipartUpload(String uploadId, String objectKey) {
        return this.completeMultipartUpload(uploadId, ossProperties.getBucketName(), objectKey);
    }

    /**
     * 取消分片上传，参考自 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/abort-mpu.html">中止分段上传</a>
     *
     * @param uploadId   上传 ID，需要先调用 {@link #initiateMultipartUpload(String, String)} 方法获取
     * @param bucketName 桶名称
     * @param objectKey  对象名称
     */
    public void abortMultipartUpload(String uploadId, String bucketName, String objectKey) {
        AbortMultipartUploadRequest abortMultipartUploadRequest = new AbortMultipartUploadRequest(bucketName, objectKey, uploadId);
        s3Client.abortMultipartUpload(abortMultipartUploadRequest);
    }

    /**
     * 取消分片上传
     *
     * @param uploadId  上传 ID，需要先调用 {@link #initiateMultipartUpload(String, String)} 方法获取
     * @param objectKey 对象名称
     */
    public void abortMultipartUpload(String uploadId, String objectKey) {
        this.abortMultipartUpload(uploadId, ossProperties.getBucketName(), objectKey);
    }

    /**
     * 列出正在进行中的分片，参考自 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/list-mpu.html">列出分段上传</a>
     *
     * @param bucketName 桶名称
     * @return 正在进行中的分片
     */
    public MultipartUploadListing listMultipartUploads(String bucketName) {
        ListMultipartUploadsRequest listMultipartUploadsRequest = new ListMultipartUploadsRequest(bucketName);
        return s3Client.listMultipartUploads(listMultipartUploadsRequest);
    }

    /**
     * 列出正在进行中的分片
     *
     * @return 正在进行中的分片
     */
    public MultipartUploadListing listMultipartUploads() {
        return this.listMultipartUploads(ossProperties.getBucketName());
    }

    /**
     * 获取文件地址，格式为：endpoint/bucketName/objectKey，例如：<a href="http://127.0.0.1:9000/test/1.txt">http://127.0.0.1:9000/test/1.txt</a>
     *
     * @param bucketName 桶名称
     * @param objectKey  对象名称
     * @return 文件地址
     */
    public String getPath(String bucketName, String objectKey) {
        return String.format("%s/%s/%s", ossProperties.getEndpoint(), bucketName, objectKey);
    }

    /**
     * 获取文件地址，格式为：endpoint/bucketName/objectKey，例如：<a href="http://127.0.0.1:9000/test/1.txt">http://127.0.0.1:9000/test/1.txt</a>
     *
     * @param objectKey 对象名称
     * @return 文件地址
     */
    public String getPath(String objectKey) {
        return this.getPath(ossProperties.getBucketName(), objectKey);
    }
}
