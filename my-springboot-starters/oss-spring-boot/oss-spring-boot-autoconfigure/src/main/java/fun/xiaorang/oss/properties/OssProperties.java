package fun.xiaorang.oss.properties;

import fun.xiaorang.oss.enums.PolicyType;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/5/24 2:55
 */
@Data
@ConfigurationProperties(prefix = "oss")
public class OssProperties implements InitializingBean {
    /**
     * 是否启用 oss，默认为 true
     */
    private boolean enable = true;

    /**
     * 对象存储服务的URL，具体取决于您使用的对象存储服务提供商
     * 例如，阿里云：<a href="https://oss-cn-hangzhou.aliyuncs.com">...</a>，参考自 <a href="https://help.aliyun.com/zh/oss/how-to-handle-the-secondleveldomainforbidden-error-message-when-you-request-oss-resources">...</a>
     * 亚马逊：<a href="https://s3.cn-north-1.amazonaws.com.cn">...</a>，参考自 <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/using-accelerate-endpoints.html">...</a>
     */
    private String endpoint;

    /**
     * 表示对象存储服务的地理区域或区域代码。不同的对象存储服务提供商可能使用不同的区域标识。例如，阿里云使用区域代码来标识不同的地理区域，如 oss-cn-hangzhou。
     * 阿里云 region 对应表 <a href="https://help.aliyun.com/document_detail/31837.htm?spm=a2c4g.11186623.0.0.695178eb0nD6jp">...</a>
     */
    private String region;

    /**
     * 账户
     */
    private String accessKey;

    /**
     * 密码
     */
    private String accessSecret;

    /**
     * true path-style nginx 反向代理和 S3 默认支持 <br/>
     * 返回 url 如：https://{endpoint}/{bucketName} <br/>
     * <br/>
     * false virtual-host-style 阿里云、七牛云等云厂商 OSS 需要配置 <br/>
     * 返回 url 如：https://{bucketName}.{endpoint}
     */
    private boolean pathStyleAccess = true;

    /**
     * 默认的存储桶名称
     */
    private String bucketName;

    /**
     * 当桶不存在时，是否自动创建桶，默认为 true
     */
    private boolean createBucketIfNotExist = true;

    /**
     * 文件外链有效期，单位：分钟，默认为 10 分钟
     */
    private int urlExpirationTime = 10;

    /**
     * 存储桶策略，默认为只读
     */
    private PolicyType policyType = PolicyType.READ_ONLY;

    @Override
    public void afterPropertiesSet() {
        Assert.hasText(endpoint, "oss.endpoint 不能为空");
        Assert.hasText(accessKey, "oss.accessKey 不能为空");
        Assert.hasText(accessSecret, "oss.accessSecret 不能为空");
        Assert.hasText(bucketName, "oss.bucketName 不能为空");
    }
}
