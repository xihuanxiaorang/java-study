package fun.xiaorang.spi.unicom;

import fun.xiaorang.spi.api.InternetService;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/6/27 23:52
 */
public class ChinaUnicom implements InternetService {
    @Override
    public void connectInternet() {
        System.out.println("connect internet by [China Unicom]");
    }
}
