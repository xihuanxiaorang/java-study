package fun.xiaorang.oss.core;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/5/24 3:23
 */
@RequiredArgsConstructor
public class OssTemplate implements OssOperations {
    private static final Logger LOGGER = LoggerFactory.getLogger(OssTemplate.class);
    private final AmazonS3 ossClient;

    @Override
    public Bucket createBucket(String bucketName) {
        if (ossClient.doesBucketExistV2(bucketName)) {
            LOGGER.info("Bucket {} already exists.", bucketName);
            return getBucket(bucketName);
        }
        return ossClient.createBucket(bucketName);
    }

    @Override
    public Bucket getBucket(String bucketName) {
        return listBuckets().stream()
                .filter(bucket -> bucket.getName().equals(bucketName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Bucket> listBuckets() {
        return ossClient.listBuckets();
    }

    @Override
    public void deleteBucket(String bucketName) {
        // Delete all objects from the bucket. This is sufficient
        // for unversioned buckets. For versioned buckets, when you attempt to delete objects, Amazon S3 inserts
        // delete markers for all objects, but doesn't delete the object versions.
        // To delete objects from versioned buckets, delete all of the object versions before deleting
        // the bucket (see below for an example).
        ObjectListing objectListing = ossClient.listObjects(bucketName);
        while (true) {
            for (S3ObjectSummary s3ObjectSummary : objectListing.getObjectSummaries()) {
                ossClient.deleteObject(bucketName, s3ObjectSummary.getKey());
            }

            // If the bucket contains many objects, the listObjects() call
            // might not return all of the objects in the first listing. Check to
            // see whether the listing was truncated. If so, retrieve the next page of objects
            // and delete them.
            if (objectListing.isTruncated()) {
                objectListing = ossClient.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }

        // Delete all object versions (required for versioned buckets).
        VersionListing versionList = ossClient.listVersions(new ListVersionsRequest().withBucketName(bucketName));
        while (true) {
            for (S3VersionSummary vs : versionList.getVersionSummaries()) {
                ossClient.deleteVersion(bucketName, vs.getKey(), vs.getVersionId());
            }

            if (versionList.isTruncated()) {
                versionList = ossClient.listNextBatchOfVersions(versionList);
            } else {
                break;
            }
        }

        // After all objects and object versions are deleted, delete the bucket.
        ossClient.deleteBucket(bucketName);
    }

    @Override
    public void putObject(String bucketName, File file) {
        String fileName = file.getName();
        LOGGER.info("uploading {} file to {} bucket...", fileName, bucketName);
        ossClient.putObject(bucketName, fileName, file);
        LOGGER.info("Done!");
    }

    @Override
    public void putObject(String bucketName, String fileName, InputStream inputStream, String contextType) {
        LOGGER.info("uploading {} file to {} bucket...", fileName, bucketName);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contextType);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata);
        ossClient.putObject(putObjectRequest);
        LOGGER.info("Done!");
    }

    @Override
    public void deleteFile(String bucketName, String fileName) {
        LOGGER.info("deleting {} file from {} bucket...", fileName, bucketName);
        ossClient.deleteObject(bucketName, fileName);
        LOGGER.info("Done!");
    }

    @Override
    public String generatePresignedUrl(String bucketName, String fileName, long expTimeMillis) {
        Date expiration = new Date(expTimeMillis);
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        return ossClient.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    @Override
    public S3Object getObject(String bucketName, String fileName) {
        return ossClient.getObject(bucketName, fileName);
    }
}
