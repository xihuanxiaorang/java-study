package fun.xiaorang.spi;

import fun.xiaorang.spi.api.InternetService;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/6/27 23:41
 */
public class ApiTest {
    @Test
    public void test_00() {
        for (InternetService internetService : ServiceLoader.load(InternetService.class)) {
            internetService.connectInternet();
        }
    }
}
