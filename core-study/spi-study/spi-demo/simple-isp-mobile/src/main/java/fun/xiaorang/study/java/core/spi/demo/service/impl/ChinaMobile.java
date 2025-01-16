package fun.xiaorang.study.java.core.spi.demo.service.impl;

import fun.xiaorang.study.java.core.spi.demo.service.InternetService;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/16 21:35
 */
public class ChinaMobile implements InternetService {
  @Override
  public void connect() {
    System.out.println("connect internet by [China Mobile]");
  }
}