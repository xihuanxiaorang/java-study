package fun.xiaorang.designpattern;

import java.io.Serializable;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/6/28 1:53
 */
public class LazyStaticInnerSingleton implements Serializable {
    private LazyStaticInnerSingleton() {
        if (SingletonHolder.INSTANCE != null) {
            throw new RuntimeException("实例已经存在，请通过 getInstance() 方法获取实例对象！");
        }
    }

    /**
     * static 关键字是为了使单例的空间共享，保证这个方法不会被重写、重载
     *
     * @return LazyStaticInnerSingleton 实例对象
     */
    public static LazyStaticInnerSingleton getInstance() {
        // 在结果返回之前，一定会先加载内部类
        return SingletonHolder.INSTANCE;
    }

    private Object readResolve() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 静态内部类，默认不加载
     */
    private static class SingletonHolder {
        private static final LazyStaticInnerSingleton INSTANCE = new LazyStaticInnerSingleton();
    }
}
