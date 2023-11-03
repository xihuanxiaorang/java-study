package fun.xiaorang.oss.config;

import fun.xiaorang.oss.properties.DocInfo;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/24 16:54
 */
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(DocInfo.class)
public class SpringDocConfig {
    private final DocInfo docInfo;

    @Bean
    public OpenAPI baseInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title(docInfo.getTitle())
                        .description(docInfo.getDescription())
                        .version(docInfo.getVersion())
                        .license(new License()
                                .name(docInfo.getLicense())
                                .url(docInfo.getLicenseUrl()))
                        .contact(new Contact()
                                .name(docInfo.getAuthor())
                                .email(docInfo.getEmail())))
                .externalDocs(new ExternalDocumentation()
                        .description(docInfo.getWebsiteName())
                        .url(docInfo.getWebsiteUrl()))
                ;
    }
}
