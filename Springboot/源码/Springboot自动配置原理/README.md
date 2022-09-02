---
title: SpringBoot 自动配置原理
tags: springboot 源码
created: 2022-09-01 17:43:09
modified: 2022-09-02 02:05:04
---

# 1、SpringBoot 的自动配置是什么？

SpringBoot 自动配置：英文是 AutoConfiguration，它是指基于你引入的 jar 包 (依赖) ，对 SpringBoot 应用进行自动配置，它为 SpringBoot 框架的 " 开箱即用 " 提供了基础支撑。

# 2、环境搭建

咱们就从一个最简单的案例开始，来一步步剖析 SpringBoot 的自动配置原理到底是如何实现的。现在新建一个项目，项目名字随便取，勾选 Spring  Web 依赖。  

> ![](attachements/Pasted%20image%2020220901231342.png)  

其实，至此一个简单的 SpringBoot 项目就已经搭建好了，不得不感叹 Spring 家族是真厉害！

# 3、SpringBoot 启动流程

## 1、启动类

找到目前项目中唯一的一个类 `SpringbootStudyDemoApplication`，也是本项目的 **启动类**，**主配置类**。启动类上方标注 **`@SpringBootApplication`** 注解，该注解非常重要，在后面会详细分析，现在留个印象。

```java
@SpringBootApplication  
public class SpringbootStudyDemoApplication {  
    public static void main(String[] args) {  
        SpringApplication.run(SpringbootStudyDemoApplication.class, args);  
    }  
}
```

咱们运行的是 `main()` 方法，那么先将注意力放到 `main()` 方法中，只看到一行代码，调用 `SpringApplication` 类的静态方法 `run()`，并且将启动类的 `Class` 对象作为参数传入方法中。

```java
public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {  
   return run(new Class<?>[] { primarySource }, args);  
}

public static ConfigurableApplicationContext run(Class<?>[] primarySources, String[] args) {  
   return new SpringApplication(primarySources).run(args);  
}
```

最终调用重载的 `run()` 方法，在方法中 `new` 一个 `SpringApplication` 对象，将启动类的 Class 对象传入构造方法中，最后再调用创建出来的 `SpringApplication` 实例对象的 `run()` 方法。

## 2、初始化 SpringApplication

```java
public SpringApplication(Class<?>... primarySources) {  
   this(null, primarySources);  
}

public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
    // 初始化类加载器
	this.resourceLoader = resourceLoader;
	// Assert 断言非空，若传入的class参数为null则打印异常并退出初始化
	Assert.notNull(primarySources, "PrimarySources must not be null");
	// 获取main方法中的args，初始化启动时配置的额外参数集合
	this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
	// 判断项目启动类型：NONE/SERVLET/REACTIVE
	this.webApplicationType = WebApplicationType.deduceFromClasspath();
	// 从 Spring 工厂获取 Bootstrap Registry Initializers
	this.bootstrapRegistryInitializers = getBootstrapRegistryInitializersFromSpringFactories();
	// 获取 Spring 工厂实例 -> 容器上下文相关的初始化器
	setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
	// 获取 Spring 工厂实例 -> 设置应用程序监听器
	setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
	// 推导主应用程序类，即从当前的栈信息中寻找main所在主类
	this.mainApplicationClass = deduceMainApplicationClass();
}
```

### 1、判断 SpringBoot 应用类型

`WebApplicationType.deduceFromClasspath();` 方法，用于判断当前 SpringBoot 应用是什么类型，可选值有 REACTIVE(响应式)、NONE、SERVLET(✔).。

```java
static WebApplicationType deduceFromClasspath() {
    if (ClassUtils.isPresent(WEBFLUX_INDICATOR_CLASS, null) && !ClassUtils.isPresent(WEBMVC_INDICATOR_CLASS, null)
        && !ClassUtils.isPresent(JERSEY_INDICATOR_CLASS, null)) {
        return WebApplicationType.REACTIVE;
    }
    for (String className : SERVLET_INDICATOR_CLASSES) {
        if (!ClassUtils.isPresent(className, null)) {
            return WebApplicationType.NONE;
        }
    }
    return WebApplicationType.SERVLET;
}
```

在构造方法中，打一个断点确认当前 SpringBoot 应用是什么类型。  

>![](attachements/Pasted%20image%2020220901234415.png)

### 2、加载所有与容器上下文相关的初始化器

```java
setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
```

当 `type = ApplicationContextInitializer` 类型时，将创建出来的实例返回并设置到 `SpringApplication` 类中的 `initializers` 属性中保存起来。

#### SpringBoot 自动配置原理

其中，**`getSpringFactoriesInstances()` 方法算是 SpringBoot 自动配置最最最核心的方法**，那么就来着重分析一下 `getSpringFactoriesInstances()` 方法，源码如下所示：

```java
private <T> Collection<T> getSpringFactoriesInstances(Class<T> type) {  
   return getSpringFactoriesInstances(type, new Class<?>[] {});  
}

private <T> Collection<T> getSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes, Object... args) {  
   ClassLoader classLoader = getClassLoader();  
   // Use names and ensure unique to protect against duplicates
   Set<String> names = new LinkedHashSet<>(SpringFactoriesLoader.loadFactoryNames(type, classLoader));
   List<T> instances = createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names);  
   AnnotationAwareOrderComparator.sort(instances);  
   return instances;  
}
```

##### 细节分析 1：获取指定类型的工厂实现的完全限定类名

在该方法中有一个关键性方法 `SpringFactoriesLoader.loadFactoryNames(type, classLoader);` **使用给定的类加载器从所有的 =="META-INF/spring.factories"== 加载给定类型的工厂实现的完全限定类名**。

```java
public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {  
   ClassLoader classLoaderToUse = classLoader;  
   if (classLoaderToUse == null) {  
      classLoaderToUse = SpringFactoriesLoader.class.getClassLoader();  
   }  
   String factoryTypeName = factoryType.getName();  
   return loadSpringFactories(classLoaderToUse).getOrDefault(factoryTypeName, Collections.emptyList());  
}
```

`loadFactoryNames()` 方法中的核心为 `loadSpringFactories()` 方法。

```java
private static Map<String, List<String>> loadSpringFactories(ClassLoader classLoader) {  
   Map<String, List<String>> result = cache.get(classLoader);  
   if (result != null) {  
      return result;  
   }  
  
   result = new HashMap<>();  
   try {  
      Enumeration<URL> urls = classLoader.getResources(FACTORIES_RESOURCE_LOCATION);  
      while (urls.hasMoreElements()) {  
         URL url = urls.nextElement();  
         UrlResource resource = new UrlResource(url);  
         Properties properties = PropertiesLoaderUtils.loadProperties(resource);  
         for (Map.Entry<?, ?> entry : properties.entrySet()) {  
            String factoryTypeName = ((String) entry.getKey()).trim();  
            String[] factoryImplementationNames =  
                  StringUtils.commaDelimitedListToStringArray((String) entry.getValue());  
            for (String factoryImplementationName : factoryImplementationNames) {  
               result.computeIfAbsent(factoryTypeName, key -> new ArrayList<>())  
                     .add(factoryImplementationName.trim());  
            }  
         }      }  
      // Replace all lists with unmodifiable lists containing unique elements  
      result.replaceAll((factoryType, implementations) -> implementations.stream().distinct()  
            .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)));  
      cache.put(classLoader, result);  
   }  
   catch (IOException ex) {  
      throw new IllegalArgumentException("Unable to load factories from location [" +  
            FACTORIES_RESOURCE_LOCATION + "]", ex);  
   }  
   return result;  
}
```

在这个方法中有一个常量 **`FACTORIES_RESOURCE_LOCATION`**，这个常量的值 = **=="META-INF/spring.factories"==**，**通过类加载器检索类路径下所有的 META-INF/spring.factories 文件**，该文件主要位于两处地方，分别在处在 `spring-boot` 和 `spring-boot-autoconfigure ` 模块的资源目录下。  

> ![|600](attachements/Pasted%20image%2020220902010442.png)  

让咱们来看看 `spring-boot-autoconfigure` 模块下的 spring.factories 文件具体长什么样子。  

>![|1000](attachements/Pasted%20image%2020220902010606.png)  
上图只截取了一部分，该文件类似于 `Properties` 配置文件，一个 key 对应多个值，每个值之间用逗号分隔。`loadSpringFactories()` 方法就是在找到所有的 **=="META-INF/spring.factories"==** 文件之后，循环遍历每个 **=="META-INF/spring.factories"==** 文件，把文件中内容的转换之后存放到一个 `Map` 集合中，最后会把这个 `Map` 集合存放一个 `cache` 缓存中，方便下次获取的时候直接从缓存中拿，而不用再重新解析 **=="META-INF/spring.factories"==** 文件获取。  
让咱们在 `loadSpringFactories()` 方法的最后打一个断点，看下返回的结果。 可知，当 key 的类型为 `ApplicationContextInitializer` 时，对应的 value 值为一个 list 集合，说明存在 7 个应用上下文初始化器。  

>![](attachements/Pasted%20image%2020220902005041.png)  

##### 细节分析 2：实例化指定类型的所有工厂实现

回到 `getSpringFactoriesInstances()` 方法，执行完 `SpringFactoriesLoader.loadFactoryNames(type, classLoader);` 方法之后，开始执行 `createSpringFactoriesInstances()` 方法。该方法就是根据 `SpringFactoriesLoader.loadFactoryNames(type, classLoader);` 方法获取出来的全限定类名通过反射技术创建出相应的实例。

```java
private <T> List<T> createSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes,  
      ClassLoader classLoader, Object[] args, Set<String> names) {  
   List<T> instances = new ArrayList<>(names.size());  
   for (String name : names) {  
      try {  
         Class<?> instanceClass = ClassUtils.forName(name, classLoader);  
         Assert.isAssignable(type, instanceClass);  
         Constructor<?> constructor = instanceClass.getDeclaredConstructor(parameterTypes);  
         T instance = (T) BeanUtils.instantiateClass(constructor, args);  
         instances.add(instance);  
      }  
      catch (Throwable ex) {  
         throw new IllegalArgumentException("Cannot instantiate " + type + " : " + name, ex);  
      }  
   }  
   return instances;  
}
```

##### 彩蛋

SpringFactories 机制类似于 Java SPI 加载机制，通过 SpringFactoriesLoader 类检索类路径下所有的 META-INF/spring.factories 文件，将文件中的内容解析封装到一个 Map 集合中。

> 对于 **Java SPI 加载机制** 不清楚的小伙伴可以查看 [Java SPI](../../../JDK/进阶/Java%20SPI.md) 这一篇文章，文章中详细地介绍了 Java SPI 加载机制是什么以及如何使用该特性进行扩展。  

### 3、加载所有的应用程序监听器

```java
this.setListeners(this.getSpringFactoriesInstances(ApplicationListener.class));
```

`getSpringFactoriesInstances(ApplicationListener.class)` 是不是很熟悉，和上面的解析过程是一模一样的，只不过换了一个 `type` 而已，这次获取的是 `ApplicationListener` 类型的值。咱们就不点进去这个方法查看了，选中 `this.getSpringFactoriesInstances(ApplicationListener.class)`，然后 **==Ctrl + U==**，计算一下方法的返回值。  

>![|1100](attachements/Pasted%20image%2020220902014422.png)  

当 `type = ApplicationListener` 类型时，将创建出来的实例返回并设置到 `SpringApplication` 类中的 `listeners` 属性中保存起来。

### 4、推导主应用程序类

该方法从当前的栈信息中寻找 `main()` 方法所在的类，为后面的包扫描做准备。

```java
private Class<?> deduceMainApplicationClass() {  
   try {  
      StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();  
      for (StackTraceElement stackTraceElement : stackTrace) {  
         if ("main".equals(stackTraceElement.getMethodName())) {  
            return Class.forName(stackTraceElement.getClassName());  
         }  
      }  
   }  
   catch (ClassNotFoundException ex) {  
      // Swallow and continue  
   }  
   return null;  
}
```

## 3、运行 SpringApplication
