package fun.xiaorang.oss.core;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">OSS操作接口，参考自官方文档：<a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide">用户指南</a><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/5/24 3:28
 */
public interface OssOperations {
    /**
     * 根据指定的桶名称创建一个桶
     *
     * @param bucketName 桶名称
     * @return 桶
     */
    Bucket createBucket(String bucketName);

    /**
     * 根据指定的桶名称获取桶
     *
     * @param bucketName 桶名称
     * @return 桶
     */
    Bucket getBucket(String bucketName);

    /**
     * 获取所有的桶
     *
     * @return 已创建的桶集合
     */
    List<Bucket> listBuckets();

    /**
     * 根据指定的桶名称删除桶
     * 参考自 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/delete-bucket.html">删除桶</a>
     *
     * @param bucketName 桶名称
     */
    void deleteBucket(String bucketName);

    /**
     * 上传文件到指定的桶中
     *
     * @param bucketName 桶名称
     * @param file       要上传的文件
     */
    void putObject(String bucketName, File file);

    /**
     * 上传文件到指定的桶中
     * 参考自 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/upload-objects.html">上传对象</a>
     *
     * @param bucketName  桶名称
     * @param fileName    文件名称
     * @param inputStream 要上传文件的输入流
     * @param contextType 文件类型
     */
    void putObject(String bucketName, String fileName, InputStream inputStream, String contextType);

    /**
     * 上传文件到指定的桶中
     *
     * @param bucketName  桶名称
     * @param fileName    文件名称
     * @param inputStream 要上传文件的输入流
     */
    default void putObject(String bucketName, String fileName, InputStream inputStream) {
        putObject(bucketName, fileName, inputStream, "application/octet-stream");
    }

    /**
     * 从指定的桶中删除文件
     *
     * @param bucketName 桶名称
     * @param fileName   文件名称
     */
    void deleteFile(String bucketName, String fileName);

    /**
     * 生成文件预览地址
     * 参考自 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/ShareObjectPreSignedURL.html">使用预签名URL共享对象</a>
     *
     * @param bucketName    桶名称
     * @param fileName      文件名称
     * @param expTimeMillis 过期时间（单位：毫秒）
     * @return 文件预览地址URL
     */
    String generatePresignedUrl(String bucketName, String fileName, long expTimeMillis);

    /**
     * 生成生成文件预览地址，默认一小时后失效
     *
     * @param bucketName 桶名称
     * @param fileName   文件名称
     * @return 文件预览地址URL
     */
    default String generatePresignedUrl(String bucketName, String fileName) {
        long expTimeMillis = Instant.now().toEpochMilli();
        expTimeMillis += 1000 * 60 * 60;
        return generatePresignedUrl(bucketName, fileName, expTimeMillis);
    }


    /**
     * 得到对象
     *
     * @param bucketName 桶名称
     * @param fileName   文件名称
     * @return {@link S3Object}
     */
    S3Object getObject(String bucketName, String fileName);
}
