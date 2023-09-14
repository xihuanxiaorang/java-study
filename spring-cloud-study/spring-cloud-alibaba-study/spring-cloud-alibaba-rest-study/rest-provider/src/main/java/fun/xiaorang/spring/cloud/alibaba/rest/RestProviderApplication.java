package fun.xiaorang.spring.cloud.alibaba.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/21 12:50
 */
@SpringBootApplication
@EnableDiscoveryClient
public class RestProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestProviderApplication.class);
    }
}
