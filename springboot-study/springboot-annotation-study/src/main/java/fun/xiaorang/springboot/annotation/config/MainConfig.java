package fun.xiaorang.springboot.annotation.config;

import fun.xiaorang.springboot.annotation.controller.OrderController;
import fun.xiaorang.springboot.annotation.pojo.*;
import fun.xiaorang.springboot.annotation.service.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/5/12 17:04
 */
@Configuration(proxyBeanMethods = false)
@Import({Color.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})
@ComponentScan(value = "fun.xiaorang.springboot.annotation", includeFilters = {
        @ComponentScan.Filter(type = ASSIGNABLE_TYPE, classes = {OrderController.class}),
        @ComponentScan.Filter(type = ASSIGNABLE_TYPE, classes = {OrderService.class}),
}, useDefaultFilters = false)
public class MainConfig {
    @Bean
    public Person person(Pet pet) {
        return new Person("xiaorang", 18, pet);
    }

    @Bean
    public Pet pet() {
        return new Pet("xiaobai", 2);
    }

    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    public Cat cat() {
        return new Cat();
    }

    @Bean
    public Teacher teacher() {
        return new Teacher();
    }

    @Bean
    public ConversionService conversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addConverter(new MyDateConverter("yyyy-MM-dd HH:mm:ss"));
        // 如果添加同种 String -> Date 的类型转换器，后面添加的会生效
        // conversionService.addConverter(new MyDateConverter());
        return conversionService;
    }
}
