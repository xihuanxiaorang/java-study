package fun.xiaorang.springboot.annotation;

import fun.xiaorang.springboot.annotation.config.MainConfig;
import fun.xiaorang.springboot.annotation.config.MyTypeFilter;
import fun.xiaorang.springboot.annotation.service.BookService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import static org.springframework.context.annotation.FilterType.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">
 * <p>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a> @Copyright
 * 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a> - show me the code
 * @date 2023/5/12 16:57
 */
//@SpringBootApplication
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(type = ASSIGNABLE_TYPE, classes = {MainConfig.class})
})
@ComponentScan(
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.CUSTOM,
                classes = {TypeExcludeFilter.class}
        ), @ComponentScan.Filter(
                type = FilterType.CUSTOM,
                classes = {AutoConfigurationExcludeFilter.class}
        )}, useDefaultFilters = false
)
@ComponentScan(value = "fun.xiaorang.springboot.annotation", includeFilters = {
        @ComponentScan.Filter(type = ANNOTATION, classes = {Controller.class}),
        @ComponentScan.Filter(type = ASSIGNABLE_TYPE, classes = {BookService.class}),
        @ComponentScan.Filter(type = CUSTOM, classes = {MyTypeFilter.class})
}, excludeFilters = {
        @ComponentScan.Filter(type = ANNOTATION, classes = Service.class)
}, useDefaultFilters = false)
public class SpringBootAnnotationStudy {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootAnnotationStudy.class, args);
    }
}
