package fun.xiaorang.oss.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import fun.xiaorang.oss.filter.DruidAdRemoveFilter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/21 19:09
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter(DruidDataSourceAutoConfigure.class)
public class DruidConfig {
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatView() {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        // 配置Druid监控的登录用户名和密码
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("loginUsername", "admin");
        initParameters.put("loginPassword", "admin");
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> druidWebStatFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>(new WebStatFilter());
        // 配置过滤规则
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }

    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.web-stat-filter.enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<DruidAdRemoveFilter> druidAdRemoveFilterFilter(DruidStatProperties properties) {
        // 获取 druid web 监控页面的参数
        DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
        // 提取 common.js 的配置路径
        String pattern = config.getUrlPattern() != null ? config.getUrlPattern() : "/druid/*";
        String commonJsPattern = pattern.replaceAll("\\*", "js/common.js");
        // 创建 DruidAdRemoveFilter Bean
        FilterRegistrationBean<DruidAdRemoveFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DruidAdRemoveFilter());
        registrationBean.addUrlPatterns(commonJsPattern);
        return registrationBean;
    }
}
