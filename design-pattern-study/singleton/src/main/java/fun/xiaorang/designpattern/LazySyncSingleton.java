package fun.xiaorang.designpattern;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/6/28 1:30
 */
public class LazySyncSingleton {
    private static LazySyncSingleton INSTANCE;

    private LazySyncSingleton() {
        // 私有构造函数
    }

    public synchronized static LazySyncSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LazySyncSingleton();
        }
        return INSTANCE;
    }
}
