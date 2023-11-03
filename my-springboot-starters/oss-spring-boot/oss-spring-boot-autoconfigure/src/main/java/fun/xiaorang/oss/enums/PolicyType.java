package fun.xiaorang.oss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/11/1 16:24
 */
@AllArgsConstructor
@Getter
public enum PolicyType {
    READ_ONLY("只读", "{\n" +
            "    \"Version\": \"2012-10-17\",\n" +
            "    \"Statement\": [\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": {\n" +
            "                \"AWS\": [\n" +
            "                    \"*\"\n" +
            "                ]\n" +
            "            },\n" +
            "            \"Action\": [\n" +
            "                \"s3:GetBucketLocation\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"arn:aws:s3:::my-defaultBucketName\"\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": {\n" +
            "                \"AWS\": [\n" +
            "                    \"*\"\n" +
            "                ]\n" +
            "            },\n" +
            "            \"Action\": [\n" +
            "                \"s3:ListBucket\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"arn:aws:s3:::my-defaultBucketName\"\n" +
            "            ],\n" +
            "            \"Condition\": {\n" +
            "                \"StringEquals\": {\n" +
            "                    \"s3:prefix\": [\n" +
            "                        \"*\"\n" +
            "                    ]\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": {\n" +
            "                \"AWS\": [\n" +
            "                    \"*\"\n" +
            "                ]\n" +
            "            },\n" +
            "            \"Action\": [\n" +
            "                \"s3:GetObject\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"arn:aws:s3:::my-defaultBucketName/**\"\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}"),
    WRITE_ONLY("只写", "{\n" +
            "    \"Version\": \"2012-10-17\",\n" +
            "    \"Statement\": [\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": {\n" +
            "                \"AWS\": [\n" +
            "                    \"*\"\n" +
            "                ]\n" +
            "            },\n" +
            "            \"Action\": [\n" +
            "                \"s3:GetBucketLocation\",\n" +
            "                \"s3:ListBucketMultipartUploads\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"arn:aws:s3:::my-defaultBucketName\"\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": {\n" +
            "                \"AWS\": [\n" +
            "                    \"*\"\n" +
            "                ]\n" +
            "            },\n" +
            "            \"Action\": [\n" +
            "                \"s3:PutObject\",\n" +
            "                \"s3:AbortMultipartUpload\",\n" +
            "                \"s3:DeleteObject\",\n" +
            "                \"s3:ListMultipartUploadParts\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"arn:aws:s3:::my-defaultBucketName/**\"\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}"),
    READ_WRITE("可读可写", "{\n" +
            "    \"Version\": \"2012-10-17\",\n" +
            "    \"Statement\": [\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": {\n" +
            "                \"AWS\": [\n" +
            "                    \"*\"\n" +
            "                ]\n" +
            "            },\n" +
            "            \"Action\": [\n" +
            "                \"s3:GetBucketLocation\",\n" +
            "                \"s3:ListBucketMultipartUploads\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"arn:aws:s3:::my-defaultBucketName\"\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": {\n" +
            "                \"AWS\": [\n" +
            "                    \"*\"\n" +
            "                ]\n" +
            "            },\n" +
            "            \"Action\": [\n" +
            "                \"s3:ListBucket\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"arn:aws:s3:::my-defaultBucketName\"\n" +
            "            ],\n" +
            "            \"Condition\": {\n" +
            "                \"StringEquals\": {\n" +
            "                    \"s3:prefix\": [\n" +
            "                        \"*\"\n" +
            "                    ]\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": {\n" +
            "                \"AWS\": [\n" +
            "                    \"*\"\n" +
            "                ]\n" +
            "            },\n" +
            "            \"Action\": [\n" +
            "                \"s3:ListMultipartUploadParts\",\n" +
            "                \"s3:PutObject\",\n" +
            "                \"s3:AbortMultipartUpload\",\n" +
            "                \"s3:DeleteObject\",\n" +
            "                \"s3:GetObject\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"arn:aws:s3:::my-defaultBucketName/**\"\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}");

    private static final String MY_DEFAULT_BUCKET_NAME = "my-defaultBucketName";

    private final String desc;
    private final String policy;

    /**
     * 获取策略
     *
     * @param bucketName 存储桶名称
     * @return 策略
     */
    public String getPolicy(String bucketName) {
        return this.policy.replace(MY_DEFAULT_BUCKET_NAME, bucketName);
    }
}
