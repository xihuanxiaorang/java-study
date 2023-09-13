package fun.xiaorang.springboot.annotation.pojo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/5/16 22:28
 */
@Slf4j
public class Cat implements InitializingBean, DisposableBean {
    @PostConstruct
    public void postConstruct() {
        log.info("postConstruct()");
    }

    @Override
    public void afterPropertiesSet() {
        log.info("InitializingBean#afterPropertiesSet()");
    }

    public void initMethod() {
        log.info("initMethod()");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("preDestroy()");
    }

    @Override
    public void destroy() {
        log.info("DisposableBean#destroy()");
    }

    public void destroyMethod() {
        log.info("destroyMethod()");
    }
}
