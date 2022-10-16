---
title: Java并发编程与多线程原理
tags: 多线程 juc
created: 2022-10-01 15:29:07
modified: 2022-10-16 07:47:28
number headings: auto, first-level 1, max 6, _.1.1.
---

sleep：

- 调用 `sleep()` 方法会让当前线程从 `Running` 进入 `Timed Waiting` 状态（阻塞）
- `sleep()` 方法的过程中，**线程不会释放对象锁**
- 其它线程可以使用 `interrupt()` 方法打断正在睡眠的线程，这时 `sleep()` 方法会抛出 `InterruptedException`
- 睡眠结束后的线程未必会立刻得到执行，需要抢占 CPU
- 建议用 `TimeUnit` 的 `sleep()` 代替 `Thread` 的 `sleep()` 来获得更好的可读性

yield：

- 调用 `yield()` 方法会提示线程调度器让出当前线程对 CPU 的使用，线程调度器可以忽略此提示
- 具体的实现依赖于操作系统的任务调度器
- **会放弃 CPU 资源，锁资源不会释放**

## 1. 死锁

### 1.1. 形成

死锁：多个线程同时被阻塞，它们中的一个或者全部都在等待某个资源被释放，由于线程被无限期地阻塞，因此程序不可能正常终止  
Java 死锁产生的四个必要条件：

1. 互斥条件，即当资源被一个线程使用（占有）时，别的线程不能使用
2. 不可剥夺条件，资源请求者不能强制从资源占有者手中夺取资源，资源只能由资源占有者主动释放
3. 请求和保持条件，即当资源请求者在请求其他的资源的同时保持对原有资源的占有
4. 循环等待条件，即存在一个等待循环队列：p1 要 p2 的资源，p2 要 p1 的资源，形成了一个等待环路  
四个条件都成立的时候，便形成死锁。死锁情况下打破上述任何一个条件，便可让死锁消失

```java
public class TestDeadLock1 {  
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDeadLock1.class);  
  
    public static void main(String[] args) {  
        Object lockA = new Object();  
        Object lockB = new Object();  
  
        new Thread(() -> {  
            LOGGER.debug("尝试获取锁A。。。");  
            synchronized (lockA) {  
                LOGGER.debug("获取到锁A");  
                try {  
                    TimeUnit.SECONDS.sleep(2);  
                    LOGGER.debug("尝试获取锁B。。。");  
                    synchronized (lockB) {  
                        LOGGER.debug("获取到锁B");  
                    }  
                } catch (InterruptedException e) {  
                    throw new RuntimeException(e);  
                }  
            }  
        }, "t1").start();  
  
        new Thread(() -> {  
            LOGGER.debug("尝试获取锁B。。。");  
            synchronized (lockB) {  
                LOGGER.debug("获取到锁B");  
                try {  
                    TimeUnit.SECONDS.sleep(2);  
                    LOGGER.debug("尝试获取锁A。。。");  
                    synchronized (lockA) {  
                        LOGGER.debug("获取到锁A");  
                    }  
                } catch (InterruptedException e) {  
                    throw new RuntimeException(e);  
                }  
            }  
        }, "t2").start();  
    }  
}
```

### 1.2. 定位

🎨方案：使用 `jps` 定位进程 `id`，再用 `jstack id` 定位死锁，找到死锁的线程去查看源码，解决并优化。  
![](attachments/Pasted%20image%2020221011041810.png)  
![](attachments/Pasted%20image%2020221011042528.png)

### 1.3. 哲学家就餐案例

有五位哲学家，围坐在圆桌旁。  

- 他们只做两件事，思考和吃饭，思考一会吃口饭，吃完饭后接着思考。  
- 吃饭时要用两根筷子吃，桌上共有 5 根筷子，每位哲学家左右手边各有一根筷子。  
- 如果筷子被身边的人拿着，自己就得等待

筷子类：

```java
class Chopstick {  
    private final String name;  
  
    public Chopstick(String name) {  
        this.name = name;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    @Override  
    public String toString() {  
        return "Chopstick{" +  
                "name='" + name + '\'' +  
                '}';  
    }  
}
```

哲学家类：

```java
class Philosopher extends Thread {  
    private static final Logger LOGGER = LoggerFactory.getLogger(Philosopher.class);  
    private final Chopstick left;  
    private final Chopstick right;  
  
    public Philosopher(String name, Chopstick left, Chopstick right) {  
        super(name);  
        this.left = left;  
        this.right = right;  
    }  
  
    @Override  
    public void run() {  
        while (true) {  
            LOGGER.debug("尝试获取左手边的筷子{}中。。。", left);  
            synchronized (left) {  
                LOGGER.debug("拿起左手边的筷子{}后，尝试获取右手边的筷子{}中。。。", left, right);  
                try {  
                    TimeUnit.SECONDS.sleep(2);  
                    synchronized (right) {  
                        LOGGER.debug("拿起右手边的筷子{}", right);  
                        eat();  
                    }  
                } catch (InterruptedException e) {  
                    throw new RuntimeException(e);  
                }  
            }  
        }  
    }  
  
    private void eat() {  
        try {  
            LOGGER.debug("两只筷子都拿到了，可以开始吃饭了！");  
            TimeUnit.SECONDS.sleep(2);  
        } catch (InterruptedException e) {  
            throw new RuntimeException(e);  
        }  
    }  
}
```

测试类：

```java
public class TestDeadLock2 {  
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDeadLock2.class);  
  
    public static void main(String[] args) {  
        Chopstick c1 = new Chopstick("c1");  
        Chopstick c2 = new Chopstick("c2");  
        Chopstick c3 = new Chopstick("c3");  
        Chopstick c4 = new Chopstick("c4");  
        Chopstick c5 = new Chopstick("c5");  
        new Philosopher("苏格拉底", c1, c2).start();  
        new Philosopher("柏拉图", c2, c3).start();  
        new Philosopher("亚里士多德", c3, c4).start();  
        new Philosopher("赫拉克利特", c4, c5).start();  
        new Philosopher("阿基米德", c5, c1).start();  
    }  
}
```

执行测试代码，测试结果如下所示：  
![](attachments/Pasted%20image%2020221011045752.png)

## 2. ReentrantLock

相对于 synchronized 它具备如下特点：

- 可中断
- 可以设置超时时间
- 可以设置为公平锁
- 支持多个条件变量  
与 synchronized 一样，都支持 **可重入**。基本语法如下所示：

```java
// 获取锁
reentrantLock.lock();
try {
 // 临界区
} finally {
 // 释放锁
 reentrantLock.unlock();
}
```

### 2.1. 可重入

可重入是指同一个线程如果首次获得了这把锁，那么它是这把锁的拥有者，因此有权利再次获取这把锁，如果不可重入锁，那么第二次获得锁时，自己也会被锁挡住，直接造成死锁。

```java
public class TestReentrantLock1 {  
    private static final Logger LOGGER = LoggerFactory.getLogger(TestReentrantLock1.class);  
    private static final ReentrantLock lock = new ReentrantLock();  
  
    public static void main(String[] args) {  
        method1();  
    }  
  
    private static void method1() {  
        lock.lock();  
        try {  
            LOGGER.debug("execute method1");  
            method2();  
        } finally {  
            lock.unlock();  
        }  
    }  
  
    private static void method2() {  
        lock.lock();  
        try {  
            LOGGER.debug("execute method2");  
        } finally {  
            lock.unlock();  
        }  
    }  
}
```

输出如下所示：  
![](attachments/Pasted%20image%2020221011050727.png)  

🔥加锁一次解锁两次：运行程序会直接报错！

```java
public void getLock() {  
	lock.lock();   
	try {  
		System.out.println(Thread.currentThread().getName() + "\t get Lock");  
	} finally {  
		lock.unlock();  
		lock.unlock();  
	}  
}
```

### 2.2. 可打断

可打断指的是处于阻塞状态等待锁的线程可以被打断等待。注意 `lock.lockInterruptibly()` 和 `lock.trylock()` 方法是可打断的，而 `lock.lock()` 不可。**可打断的意义在于避免得不到锁的线程无限制地等待下去，防止死锁的一种方式**。  

```java
public class TestReentrantLock2 {  
    private static final Logger LOGGER = LoggerFactory.getLogger(TestReentrantLock2.class);  
    private static final ReentrantLock lock = new ReentrantLock();  
  
    public static void main(String[] args) {  
        Thread t1 = new Thread(() -> {  
            try {  
                LOGGER.debug("尝试获取锁");  
                lock.lockInterruptibly();  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
                LOGGER.debug("被打断，没有获取到锁，直接返回");  
                return;            }  
            try {  
                LOGGER.debug("获取到锁");  
            } finally {  
                lock.unlock();  
            }  
        }, "t1");  
        lock.lock();  
        try {  
            LOGGER.debug("获取到锁");  
            t1.start();  
            TimeUnit.SECONDS.sleep(2);  
            LOGGER.debug("执行打断获取锁");  
            t1.interrupt();  
        } catch (InterruptedException e) {  
            throw new RuntimeException(e);  
        } finally {  
            lock.unlock();  
        }  
    }  
}
```

输出如下所示：  
![](attachments/Pasted%20image%2020221011052911.png)

### 2.3. 锁超时

#### 2.3.1. 基本使用

`public boolean tryLock()`：尝试获取锁，获取到返回 true，获取不到直接放弃，不进入阻塞队列  

`public boolean tryLock(long timeout, TimeUnit unit)`：在给定时间内获取锁，获取不到就退出

💡注意：tryLock 期间也可以被打断。

```java
public class TestReentrantLock3 {  
    private static final Logger LOGGER = LoggerFactory.getLogger(TestReentrantLock3.class);  
    private static final ReentrantLock lock = new ReentrantLock();  
  
    public static void main(String[] args) {  
        Thread t1 = new Thread(() -> {  
            try {  
                LOGGER.debug("尝试获取锁");  
                if (!lock.tryLock(2, TimeUnit.SECONDS)) {  
                    LOGGER.debug("等待了这么久，还是没有获取到锁，算啦~");  
                    return;                }  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
                LOGGER.debug("被打断，没有获取到锁，直接返回");  
                return;            }  
            try {  
                LOGGER.debug("获取到锁");  
            } finally {  
                lock.unlock();  
            }  
        }, "t1");  
        lock.lock();  
        try {  
            LOGGER.debug("获取到锁");  
            t1.start();  
            TimeUnit.SECONDS.sleep(3);  
        } catch (InterruptedException e) {  
            throw new RuntimeException(e);  
        } finally {  
            LOGGER.debug("释放锁");  
            lock.unlock();  
        }  
    }  
}
```

#### 2.3.2. 哲学家就餐问题

筷子类：

```java
class Chopstick2 extends ReentrantLock {  
    private final String name;  
  
    public Chopstick2(String name) {  
        this.name = name;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    @Override  
    public String toString() {  
        return "Chopstick{" +  
                "name='" + name + '\'' +  
                '}';  
    }  
}
```

哲学家类：

```java
class Philosopher2 extends Thread {  
    private static final Logger LOGGER = LoggerFactory.getLogger(Philosopher.class);  
    private final Chopstick2 left;  
    private final Chopstick2 right;  
  
    public Philosopher2(String name, Chopstick2 left, Chopstick2 right) {  
        super(name);  
        this.left = left;  
        this.right = right;  
    }  
  
    @Override  
    public void run() {  
        while (true) {  
            if (left.tryLock()) {  
                try {  
                    if (right.tryLock()) {  
                        try {  
                            eat();  
                        } finally {  
                            right.unlock();  
                        }  
                    }  
                } finally {  
                    left.unlock();  
                }  
            }  
        }  
    }  
  
    private void eat() {  
        try {  
            LOGGER.debug("两只筷子都拿到了，可以开始吃饭了！");  
            TimeUnit.SECONDS.sleep(2);  
        } catch (InterruptedException e) {  
            throw new RuntimeException(e);  
        }  
    }  
}
```

测试类：

```java
public class TestDeadLock3 {  
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDeadLock3.class);  
  
    public static void main(String[] args) {  
        Chopstick2 c1 = new Chopstick2("c1");  
        Chopstick2 c2 = new Chopstick2("c2");  
        Chopstick2 c3 = new Chopstick2("c3");  
        Chopstick2 c4 = new Chopstick2("c4");  
        Chopstick2 c5 = new Chopstick2("c5");  
        new Philosopher2("苏格拉底", c1, c2).start();  
        new Philosopher2("柏拉图", c2, c3).start();  
        new Philosopher2("亚里士多德", c3, c4).start();  
        new Philosopher2("赫拉克利特", c4, c5).start();  
        new Philosopher2("阿基米德", c5, c1).start();  
    }  
}
```

执行测试代码，测试结果如下所示：  
![](attachments/Pasted%20image%2020221011055351.png)

### 2.4. 条件变量

synchronized 的条件变量，是当条件不满足时进入 WaitSet 等待；ReentrantLock 的条件变量比 synchronized 强大之处在于支持多个条件变量。

- synchronized 是那些不满足条件的线程都在一间休息室等消息
- 而 ReentrantLock 支持多间休息室，有专门等烟的休息室、专门等早餐的休息室、唤醒时也是按休息室来唤醒  

通过 ReentrantLock 类中的 `newCondition()` 方法获取 Condition 对象，其中 Condition 类的 API：

- `void await()`：当前线程从运行状态进入等待状态，释放锁
- `void signal()`：唤醒一个等待在 Condition 上的线程，但是必须获得与该 Condition 相关的锁  

使用流程：

- **await / signal 前需要获得锁**
- await 执行后，会释放锁进入 ConditionObject 等待
- await 的线程被唤醒去重新竞争 lock 锁
- **线程在条件队列被打断会抛出中断异常**
- 竞争 lock 锁成功后，从 await 后继续执行

```java
public class TestReentrantLock4 {  
    private static final Logger LOGGER = LoggerFactory.getLogger(TestReentrantLock4.class);  
    private static final ReentrantLock lock = new ReentrantLock();  
    private static final Condition waitCigaretteRoom = lock.newCondition();  
    private static final Condition waitTakeoutRoom = lock.newCondition();  
    private static boolean hasCigarette = false; // 有没有烟  
    private static boolean hasTakeout = false; // 有没有外卖  
  
    public static void main(String[] args) throws InterruptedException {  
        new Thread(() -> {  
            lock.lock();  
            try {  
                while (!hasCigarette) {  
                    LOGGER.debug("没烟没思路，休息。。。！");  
                    try {  
                        waitCigaretteRoom.await();  
                    } catch (InterruptedException e) {  
                        throw new RuntimeException(e);  
                    }  
                }  
                LOGGER.debug("终于买到烟了！");  
                if (hasCigarette) {  
                    LOGGER.debug("思如泉涌！事半功倍");  
                } else {  
                    LOGGER.debug("死气沉沉！干不了活");  
                }  
            } finally {  
                lock.unlock();  
            }  
        }, "小南").start();  
  
        new Thread(() -> {  
            lock.lock();  
            try {  
                while (!hasTakeout) {  
                    LOGGER.debug("肚子好饿，休息。。。！");  
                    try {  
                        waitTakeoutRoom.await();  
                    } catch (InterruptedException e) {  
                        throw new RuntimeException(e);  
                    }  
                }  
                LOGGER.debug("外卖终于终于到了！");  
                if (hasTakeout) {  
                    LOGGER.debug("吃饱喝足！开干");  
                } else {  
                    LOGGER.debug("肚子都饿扁了！干不了活");  
                }  
            } finally {  
                lock.unlock();  
            }  
        }, "小女").start();  
  
        TimeUnit.SECONDS.sleep(1);  
  
        new Thread(() -> {  
            lock.lock();  
            try {  
                LOGGER.debug("您的外卖已送到！");  
                hasTakeout = true;  
                waitTakeoutRoom.signal();  
            } finally {  
                lock.unlock();  
            }  
        }, "送外卖的").start();  
  
        TimeUnit.SECONDS.sleep(1);  
  
        new Thread(() -> {  
            lock.lock();  
            try {  
                LOGGER.debug("卖烟啦~~快来买呀！");  
                hasCigarette = true;  
                waitCigaretteRoom.signal();  
            } finally {  
                lock.unlock();  
            }  
        }, "卖烟的").start();  
    }  
}
```

输出结果如下所示：  
![](attachments/Pasted%20image%2020221011062216.png)

## 3. synchronized
