package fun.xiaorang.oss.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/5/24 2:55
 */
@Data
@ConfigurationProperties(prefix = "oss")
public class OssProperties {
    private String accessKey;
    private String accessSecret;
    /**
     * endpoint 配置格式为
     * 通过外网访问OSS服务时，以URL的形式表示访问的OSS资源，详情请参见OSS访问域名使用规则。OSS的URL结构为[$Schema]://[$Bucket].[$Endpoint]/[$Object]
     * 。例如，您的Region为华东1（杭州），Bucket名称为 examplebucket，Object访问路径为destfolder/example.txt，
     * 则外网访问地址为https://examplebucket.oss-cn-hangzhou.aliyuncs.com/destfolder/example.txt
     * <a href="https://help.aliyun.com/document_detail/375241.html">...</a>
     */
    private String endpoint;
    /**
     * refer com.amazonaws.regions.Regions;
     * 阿里云 region 对应表
     * <a href="https://help.aliyun.com/document_detail/31837.htm?spm=a2c4g.11186623.0.0.695178eb0nD6jp">...</a>
     */
    private String region;
    private boolean pathStyleAccess = true;
}
