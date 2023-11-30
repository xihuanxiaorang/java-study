package fun.xiaorang.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/11/29 17:39
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                // 配置当前资源服务器的资源 id，默认为 oauth2-resource，
                // 只有客户端所拥有的资源包含当前资源服务器的资源 id 时，才能访问当前资源服务器
                .resourceId("res1")
                .tokenStore(tokenStore())
                .stateless(true);
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.
                csrf()
                .disable()
                .authorizeRequests()
                // 配置当前资源服务器必须满足 scope1 的权限才能访问
                .antMatchers("/**").access("#oauth2.hasScope('scope1')")
                // 配置当前资源服务器任何请求都必须认证后才能访问
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 通过读取本地文件获取非对称加密公钥，用于对 JWT 进行解密
        converter.setVerifierKey(getPublicKey());
        return converter;
    }

    private String getPublicKey() {
        final Resource resource = new ClassPathResource("public.txt");
        String publicKey = null;
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                publicKey = br.lines().collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
}
