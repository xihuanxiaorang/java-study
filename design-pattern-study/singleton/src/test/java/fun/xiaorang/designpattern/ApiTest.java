package fun.xiaorang.designpattern;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/6/28 0:08
 */
class ApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_00() {
        HungrySingleton hungrySingleton1 = HungrySingleton.getInstance();
        HungrySingleton hungrySingleton2 = HungrySingleton.getInstance();
        LOGGER.info("{}", hungrySingleton1);
        LOGGER.info("{}", hungrySingleton2);
    }

    @Test
    public void test_01() {
        Runnable runnable = () -> {
            LazySingleton instance = LazySingleton.getInstance();
            LOGGER.info("{}：{}", Thread.currentThread().getName(), instance);
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
    }

    @Test
    public void test_02() {
        Runnable runnable = () -> {
            LazySyncSingleton instance = LazySyncSingleton.getInstance();
            LOGGER.info("{}：{}", Thread.currentThread().getName(), instance);
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
    }

    @Test
    public void test_03() {
        Runnable runnable = () -> {
            LazyDoubleCheckSingleton instance = LazyDoubleCheckSingleton.getInstance();
            LOGGER.info("{}：{}", Thread.currentThread().getName(), instance);
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
    }

    @Test
    public void test_04() {
        Runnable runnable = () -> {
            LazyStaticInnerSingleton instance = LazyStaticInnerSingleton.getInstance();
            LOGGER.info("{}：{}", Thread.currentThread().getName(), instance);
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
    }

    @Test
    public void test_05() {
        // 测试反射破坏单例
        Class<LazyStaticInnerSingleton> clazz = LazyStaticInnerSingleton.class;
        try {
            // 通过反射的方式获取私有的构造方法
            Constructor<LazyStaticInnerSingleton> constructor = clazz.getDeclaredConstructor();
            // 强制访问
            constructor.setAccessible(true);
            // 暴力初始化
            LazyStaticInnerSingleton o1 = constructor.newInstance();
            LOGGER.info("{}", o1);

            LazyStaticInnerSingleton o2 = LazyStaticInnerSingleton.getInstance();
            LOGGER.info("{}", o2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_06() {
        LazyStaticInnerSingleton o1 = LazyStaticInnerSingleton.getInstance();
        LOGGER.info("{}", o1);
        try (FileOutputStream fos = new FileOutputStream("LazyStaticInnerSingleton.obj");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(o1);
            oos.flush();
            try (FileInputStream fis = new FileInputStream("LazyStaticInnerSingleton.obj");
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                LazyStaticInnerSingleton o2 = (LazyStaticInnerSingleton) ois.readObject();
                LOGGER.info("{}", o2);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_07() {
        Class<EnumSingleton> clazz = EnumSingleton.class;
        try {
            Constructor<EnumSingleton> constructor = clazz.getDeclaredConstructor(String.class, int.class);
            constructor.setAccessible(true);
            EnumSingleton o1 = constructor.newInstance("instance", 1);
            EnumSingleton o2 = constructor.newInstance("instance", 2);
            LOGGER.info("{}", o1);
            LOGGER.info("{}", o2);
            LOGGER.info("{}", o1 == o2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_08() {
        Object obj = new Object();
        EnumSingleton o1 = EnumSingleton.INSTANCE;
        o1.setData(obj);
        try (FileOutputStream fos = new FileOutputStream("EnumSingleton.obj");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(o1);
            oos.flush();
            try (FileInputStream fis = new FileInputStream("EnumSingleton.obj");
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                EnumSingleton o2 = (EnumSingleton) ois.readObject();

                LOGGER.info("{}", o1.getData());
                LOGGER.info("{}", o2.getData());
                LOGGER.info("{}", o1.getData() == o2.getData());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_09() {
        LOGGER.info("{}", ThreadLocalSingleton.getInstance());
        LOGGER.info("{}", ThreadLocalSingleton.getInstance());
        Runnable runnable = () -> {
            ThreadLocalSingleton instance = ThreadLocalSingleton.getInstance();
            LOGGER.info("{}：{}", Thread.currentThread().getName(), instance);
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
    }
}