package fun.xiaorang.designpattern;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/6/28 17:51
 */
public class ThreadLocalSingleton {
    private static final ThreadLocal<ThreadLocalSingleton> TL = ThreadLocal.withInitial(ThreadLocalSingleton::new);

    private ThreadLocalSingleton() {
        // 私有构造函数
    }

    public static ThreadLocalSingleton getInstance() {
        return TL.get();
    }
}
