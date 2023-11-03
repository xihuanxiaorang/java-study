package fun.xiaorang.oss;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import fun.xiaorang.oss.core.OssTemplate;
import fun.xiaorang.oss.properties.OssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/5/24 2:53
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnWebApplication
@ConditionalOnClass(AmazonS3.class)
@ConditionalOnProperty(prefix = "oss", value = "enable", matchIfMissing = true)
public class OssAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AmazonS3 ossClient(OssProperties ossProperties) {
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(ossProperties.getAccessKey(),
                ossProperties.getAccessSecret()));
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(ossProperties.getEndpoint(),
                ossProperties.getRegion());
        return AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(awsCredentialsProvider)
                .withPathStyleAccessEnabled(ossProperties.isPathStyleAccess())
                .disableChunkedEncoding()
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(OssTemplate.class)
    @ConditionalOnBean(AmazonS3.class)
    public OssTemplate ossTemplate(AmazonS3 amazonS3, OssProperties ossProperties) {
        return new OssTemplate(amazonS3, ossProperties);
    }
}
