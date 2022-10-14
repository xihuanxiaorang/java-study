---
title: Java基础面试题
tags: 面试题 基础
created: 2022-10-11 01:04:15
modified: 2022-10-14 17:09:21
number headings: auto, first-level 1, max 6, _.1.1.
---

## 1. Integer a1 = 100，Integer a2 = 100，Integer a3 = 128，Integer a4 = 128，int a5 = 128，描述 a1 == a2 、a3 == a4 以及 a3 == a5 的运行结果以及原因 #基础 

测试代码如下：

```java
public class Test1 {  
    private static final Logger LOGGER = LoggerFactory.getLogger(Test1.class);  
  
    public static void main(String[] args) {  
        Integer a1 = 100;  
        Integer a2 = 100;  
        Integer a3 = 128;  
        Integer a4 = 128;  
        int a5 = 128;  
        LOGGER.debug("a1 == a2？{}", a1 == a2);  
        LOGGER.debug("a3 == a4？{}", a3 == a4);  
        LOGGER.debug("a3 == a5？{}", a3 == a5);  
    }  
}
```

运行结果如下所示：

```
2022-10-14 15:55:56.375 DEBUG Test1:22 - a1 == a2？true
2022-10-14 15:55:56.376 DEBUG Test1:23 - a3 == a4？false
2022-10-14 15:55:56.376 DEBUG Test1:24 - a3 == a5？true
```

### 1.1. 原理

#### 1.1.1. == 比较的是两个对象的引用地址

用 == 比较的是两个对象的引用地址，如果两个对象的引用地址相同，则 == 比较后返回的结果为 true；如果不同的话，则比较后的结果为 false。  

#### 1.1.2. Integer 对象的自动拆装箱操作

自动装箱：就是将基本数据类型自动转换成对应的包装类；自动拆箱：就是将包装类自动转换成对应的基本类型数据。

```java
Integer i = 10; // 自动装箱
int b = i; // 自动拆箱
```

对以上代码进行反编译后可以得到如下代码：

```java
Integer i = Integer.valueOf(10);  
int b = i.intValue();
```

从上面反编译后的代码可以看出，`int` 的自动装箱是通过 `Integer` 类中的 `valueOf()` 方法来实现的，而 `Integer` 自动拆箱都是通过 `intValue()` 方法来实现的。感兴趣的小伙伴，可以试着将八种基本数据类型都照这样反编译一遍，你就会发现一个规律：**自动装箱都是通过包装类的 `valueOf()` 方法来实现的，自动拆箱都是通过包装类的 `xxxValue()` 方法来实现的**。

#### 1.1.3. Integer 对象的 valueOf 方法

>`Integer` 对象中的 `valueOf()` 方法用到了享元模式，会返回缓存对象而不是创建新的对象。对于 **享元设计模式** 不清楚的小伙伴可以查看 [享元模式](../../设计模式/享元模式.md) 这一篇文章，文章中详细地介绍了使用享元模式的目的以及如何实现享元模式。  

```java
public static Integer valueOf(int i) {  
    if (i >= IntegerCache.low && i <= IntegerCache.high)  
        return IntegerCache.cache[i + (-IntegerCache.low)];  
    return new Integer(i);  
}
```

在 `Integer` 包装类中有一个私有的静态内部类 `IntegerCache`，在这个类中缓存了从 [-128, 127] 之间的所有的 `Integer` 对象，并将其存放在 `cache` 数组中。

```java
private static class IntegerCache {  
    static final int low = -128;  
    static final int high;  
    static final Integer[] cache;  
    static Integer[] archivedCache;  
  
    static {  
        // high value may be configured by property  
        int h = 127;  
        String integerCacheHighPropValue =  
            VM.getSavedProperty("java.lang.Integer.IntegerCache.high");  
        if (integerCacheHighPropValue != null) {  
            try {  
                h = Math.max(parseInt(integerCacheHighPropValue), 127);  
                // Maximum array size is Integer.MAX_VALUE  
                h = Math.min(h, Integer.MAX_VALUE - (-low) -1);  
            } catch( NumberFormatException nfe) {  
                // If the property cannot be parsed into an int, ignore it.  
            }  
        }  
        high = h;  
  
        // Load IntegerCache.archivedCache from archive, if possible  
        CDS.initializeFromArchive(IntegerCache.class);  
        int size = (high - low) + 1;  
  
        // Use the archived cache if it exists and is large enough  
        if (archivedCache == null || size > archivedCache.length) {  
            Integer[] c = new Integer[size];  
            int j = low;  
            for(int i = 0; i < c.length; i++) {  
                c[i] = new Integer(j++);  
            }  
            archivedCache = c;  
        }  
        cache = archivedCache;  
        // range [-128, 127] must be interned (JLS7 5.1.7)  
        assert IntegerCache.high >= 127;  
    }  
  
    private IntegerCache() {}  
}
```

![|502](attachments/Pasted%20image%2020221014162309.png)  
通过上述源码不难发现，在 `Integer` 的 `valueOf()` 方法中，如果数值在 [-128, 127] 范围的时候，就会去 `IntegerCache` 类中的 `cache` 数组中查找对应 `Integer` 对象的引用并返回；如果数值超过了规定的范围，则会新建一个 `Integer` 对象返回。  

### 1.2. 分析

a1 和 a2 会先各自调用 `Integer` 中的 `valueOf()` 进行装箱操作，因为 a1 和 a2 的数值都在 [-128, 127] 之间，所以会从事先已经初始化好的 `cache` 数组中直接获取，由于变量 a1 和 a2 的数值一样，所以从数组中取出的是同一个 `Integer` 对象，所以它俩用 == 比较返回的结果是 true。  

---

a2 和 a3 也会先调用 `Integer` 中的 `valueOf()` 进行装箱操作，但是值却不在 [-128, 127] 之间，因此不是从数组中获取，而是各自实例化一个新的 `Integer` 对象返回，此时这两个对象的引用地址肯定不同，所以它俩用== 比较返回的结果是 false。  
a3 和 a5 比较的时候

---

```java
public class Test1 {  
  private static final Logger LOGGER = LoggerFactory.getLogger(Test1.class);  
    
  public static void main(String[] args) {  
    Integer a1 = Integer.valueOf(100);  
    Integer a2 = Integer.valueOf(100);  
    Integer a3 = Integer.valueOf(128);  
    Integer a4 = Integer.valueOf(128);  
    int a5 = 128;  
    LOGGER.debug("a1 == a2？{}", Boolean.valueOf((a1 == a2)));  
    LOGGER.debug("a3 == a4？{}", Boolean.valueOf((a3 == a4)));  
    LOGGER.debug("a3 == a5？{}", Boolean.valueOf((a3.intValue() == a5)));  
  }  
}
```

执行反编译之后可以发现，执行 a3 == a5 的时候，会先执行 a3 的自动拆箱操作，调用 `Integer` 的 `intValue()`，由于比较的是两个基本数据类型并且数字又相等，所以它俩用== 比较返回的结果是 true。

### 1.3. 扩展

如果将上面的包装类型换成 `Double` 类型，

```java
public class Test1 {  
    private static final Logger LOGGER = LoggerFactory.getLogger(Test1.class);  
  
    public static void main(String[] args) {  
        Double a1 = 100.0;  
        Double a2 = 100.0;  
        Double a3 = 128.0;  
        Double a4 = 128.0;  
        int a5 = 128;  
        LOGGER.debug("a1 == a2？{}", a1 == a2);  
        LOGGER.debug("a3 == a4？{}", a3 == a4);  
        LOGGER.debug("a3 == a5？{}", a3 == a5);  
    }  
}
```

运行结果会是怎样呢？如下所示：

```
2022-10-14 16:48:56.684 DEBUG Test1:22 - a1 == a2？false
2022-10-14 16:48:56.685 DEBUG Test1:23 - a3 == a4？false
2022-10-14 16:48:56.685 DEBUG Test1:24 - a3 == a5？true
```

为什么会这样呢？咱们看下 `Double` 类中的 `valueOf()` 方法的源码就不难发现原因。

```java
public static Double valueOf(double d) {  
    return new Double(d);  
}
```

可以看到，`Double` 并没有 `Integer` 的缓存机制，而是直接返回了一个新的 `Double` 对象，所以如果用 == 比较的话，两个对象的引用地址不一样，自然返回 false。  
为什么 `Double` 类中的 `valueOf()` 方法会采用与 `Integer` 类中的 `valueOf()` 方法不同的实现呢？很简单，在某个范围内的整数型数值的个数是有限的，而浮点数却不是。  
查看了一下其他几种数据类型的 `valueOf()` 源码，其中，`Double` 和 `Float` 没有缓存机制，都是直接返回新的对象；`Integer`、`Short`、`Byte`、`Character` 都有缓存机制。  
至于 `Boolean` 类型，它的 `valueOf()` 方法如下：

```java
public static Boolean valueOf(boolean b) {  
    return (b ? TRUE : FALSE);  
}
```

其中的 `TRUE` 和 `FALSE`，代表两个静态成员属性。

```java
/**
 * The {@code Boolean} object corresponding to the primitive
 * value {@code true}.
 */
public static final Boolean TRUE = new Boolean(true);

/**
 * The {@code Boolean} object corresponding to the primitive
 * value {@code false}.
 */
public static final Boolean FALSE = new Boolean(false);
```

聪明的小伙伴肯定已经知道，用 == 比较两个 `Boolean` 类型的对象时，只要值相等，那么返回的结果就是 true；值如果不相等的话，返回的结果就是 false。
