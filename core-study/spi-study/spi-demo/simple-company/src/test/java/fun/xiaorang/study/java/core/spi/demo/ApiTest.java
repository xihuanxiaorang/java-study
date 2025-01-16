package fun.xiaorang.study.java.core.spi.demo;

import fun.xiaorang.study.java.core.spi.demo.service.InternetService;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/16 21:43
 */
public class ApiTest {
  @Test
  public void test() {
    final ServiceLoader<InternetService> serviceLoader = ServiceLoader.load(InternetService.class);
    serviceLoader.forEach(InternetService::connect);
  }
}
