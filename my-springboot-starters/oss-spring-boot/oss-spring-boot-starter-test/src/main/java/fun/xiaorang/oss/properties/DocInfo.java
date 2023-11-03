package fun.xiaorang.oss.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/24 16:58
 */
@Data
@ConfigurationProperties(prefix = "doc.info")
public class DocInfo {
    private String title;
    private String description;
    private String version = "v0.0.1";
    private String license = "Apache License, Version 2.0";
    private String licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0";
    private String author = "小让";
    private String email = "15019474951@163.com";
    private String websiteName = "小让の糖果屋";
    private String websiteUrl = "https://blog.xiaorang.fun";
}
