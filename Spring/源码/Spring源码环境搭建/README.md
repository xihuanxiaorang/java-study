---
title: Spring源码环境搭建
tags: spring 源码
created: 2022-08-30 02:01:56
modified: 2022-09-18 17:30:08
number headings: auto, first-level 1, max 6, _.1.1.
---

## 1. 源码下载

spring 源码地址为：[https://github.com/spring-projects/spring-framework](https://github.com/spring-projects/spring-framework)  
可以直接使用 `git clone https://github.com/spring-projects/spring-framework.git` 命令下载或者直接下载压缩包。  
由于某些原因，可能有的小伙伴下载起来会非常的慢！所以，建议先将项目从 Github 中导入到 Gitee 中，然后直接从 Gitee 中下载，你会发现下载速度非常快！🚀🚀🚀  
![|1000](attachments/Pasted%20image%2020220830022514.png)  
![|800](attachments/Pasted%20image%2020220830022717.png)  
导入完成之后，就可以开始下载，下载源码方式分为两种，可以使用 git 命令或者 zip 压缩包（这样 git 就只会有自己的提交记录），本人是选择 **直接下载的 zip 压缩包** ，版本选的是 **5.3.x** 版本，。  
![|250](attachments/Pasted%20image%2020220918155702.png)  

## 2. 配置

本人使用的 IDEA 版本是 2022 版，使用 IDEA 导入下载好的 Spring 项目，默认会开始编译，此时 **先停止编译**，等配置完成之后再开始编译项目，否则会需要很长时间。  
![](attachments/Pasted%20image%2020220830024127.png)  
Spring 源码使用 gradle 来构建编译，在编译过程中需要下载一堆的插件和 jar 包，众所周知，下载的资源都是从国外下载，如果不使用国内源来下载，怕是编译时黄花菜都凉了，所以在这里得先配置将源换到国内的源，阿里云仓库：[https://developer.aliyun.com/mvn/guide?spm=a2c6h.13651104.0.0.435836a4x5Dhns](https://developer.aliyun.com/mvn/guide?spm=a2c6h.13651104.0.0.435836a4x5Dhns).  
![](attachments/Pasted%20image%2020220830024553.png)

```gradle
maven { url "https://maven.aliyun.com/repository/public" }  
maven { url "https://maven.aliyun.com/repository/gradle-plugin" } 
```

打开项目根目录下的 `build.gradle` 和 `settings.gradle` 文件，使用快捷键找到文件中 `repositories` 关键字所在的位置，将上面的内容复制粘贴到此处，如：  
`settings.gradle`：

```gradle
pluginManagement {  
   repositories {  
      maven { url "https://maven.aliyun.com/repository/public" }  
      maven { url "https://maven.aliyun.com/repository/gradle-plugin" }  
      mavenCentral()  
      gradlePluginPortal()  
      maven { url "https://repo.spring.io/release" }  
   }  
}
```

`build.gradle`：

```gradle
repositories {  
   maven { url "https://maven.aliyun.com/repository/public" }  
   maven { url "https://maven.aliyun.com/repository/gradle-plugin" }  
   mavenCentral()  
   maven { url "https://repo.spring.io/libs-spring-framework-build" }  
}
```

等 `build.gradle` 和 `settings.gradle` 文件配置好后，开始配置 gradle，将 IDEA 中的 gradle 指定到自己下载的 gradle，配置步骤如下所示：  
![|600](attachments/Pasted%20image%2020220830025606.png)<br />  
![](attachments/Pasted%20image%2020220916040709.png)  
配置好 gradle 之后，开始编译。编译的时间长短可能与小伙伴的网速有关，因为要下载大量的 jar 包和插件，不过咱们配置了阿里云镜像，再慢也不会慢到哪里去 ，静静等待即可。等出现 BUILD SUCCESSFUL 字样就表示已经编程成功。  
![](attachments/Pasted%20image%2020220918160640.png)  
编译成功之后，使用 gradle 测试一下。  
![|400](attachments/Pasted%20image%2020220918161114.png)<br />  
双击点击执行，在执行过程中发现报错，其实是因为 `isAccessible()` 方法被弃用了，咱们把这个方法改成 `canAccess(null)` 方法。  
![](attachments/Pasted%20image%2020220830031806.png)  
再测试一下，发现执行成功！最后会提示 'git' 相关错误，但是不影响使用。  
![](attachments/Pasted%20image%2020220918161325.png)  
上面关于 git 的错误的意思是当前不是一个 git 仓库。这个好办，咱们直接使用 `git init` 命令建一个 git 仓库就好，然后再使用 `git add .` 将文件添加到暂存区，最后使用 `git commit -m "fix:  git command error"` 提交到仓库，有需要的小伙伴还可以在 Github 或者 Gitee 建立一个远程仓库，然后将代码推送到远程仓库中。

## 3. 测试案例

源码环境到底有没有成功呢？咱们就来写两个简单的案例来测试一下，一个是关于 AOP 的案例，另一个是关于 SpringMVC 的案例。  
本章节的测试案例代码都放在 [GitHub - xihuanxiaorang/spring: Spring 源码注释](https://github.com/xihuanxiaorang/spring) 上，以后的源码分析、添加注释等都会基于该项目。如果不想自己搭建并且又想后续添加自己注释的小伙伴可以获取 [Release v1.0.0 · xihuanxiaorang/spring · GitHub](https://github.com/xihuanxiaorang/spring/releases/tag/v1.0.0) 版本，该版本仅仅添加了以上两个测试案例而已，还没有添加任何源码注释。

### 3.1. SpringAOP 简单案例

选中项目右键新建一个模块，选择 Gradle，点击下一步，模块名填自己喜欢的即可，这里我就填 `spring-aop-study`，最后点击确定即可。  
![|600](attachments/Pasted%20image%2020220918162748.png)<br />  
模块建好之后，在模块的 `build.gradle` 文件中引入以下依赖：

```gradle
dependencies {  
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'  
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'
    implementation(project(':spring-context'))
    implementation(project(':spring-aspects'))
    implementation 'log4j:log4j:1.2.17'
    implementation 'org.slf4j:slf4j-log4j12:2.0.0'
    implementation 'org.slf4j:slf4j-api:2.0.0'
}
```

由于引入了 `Log4j`，所以需要在资源目录 `resources` 下创建一个 `log4j.properties` 配置文件：

```properties
### 配置根  
log4j.rootLogger=debug,console  
### 日志输出到控制台显示  
log4j.appender.console=org.apache.log4j.ConsoleAppender  
log4j.appender.console.Target=System.out  
log4j.appender.console.layout=org.apache.log4j.PatternLayout  
log4j.appender.console.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n
```

开始编写测试代码，先定义一个切面类 `LogAspect`：

```java
@Component  
@Aspect  
public class LogAspect {  
   private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);  
  
   public LogAspect() {  
      LOGGER.info("...LogAspect创建了...");  
   }  
  
   @Pointcut("execution(* top.xiaorang.aop.service.HelloService.sayHello(..))")  
   public void pointcut() {  
   }   /**  
    * 前置通知，增强方法/增强器  
    *  
    * @param joinPoint 封装了 AOP 中切面方法的信息  
    */  
   @Before("pointcut()")  
   public void logStart(JoinPoint joinPoint) {  
      String name = joinPoint.getSignature().getName();  
      LOGGER.info("前置通知logStart==>{}===【args:{}}】", name, Arrays.asList(joinPoint.getArgs()));  
   }  
  
   /**  
    * 返回通知  
    *  
    * @param joinPoint 封装了 AOP 中切面方法的信息  
    * @param result    目标方法的返回值  
    */  
   @AfterReturning(value = "pointcut()", returning = "result")  
   public void logReturn(JoinPoint joinPoint, Object result) {  
      String name = joinPoint.getSignature().getName();  
      LOGGER.info("返回通知logReturn==>{}===【args:{}}】【result：{}】", name, Arrays.asList(joinPoint.getArgs()), result);  
   }  
  
   /**  
    * 后置通知  
    *  
    * @param joinPoint 封装了 AOP 中切面方法的信息  
    */  
   @After("pointcut()")  
   public void logEnd(JoinPoint joinPoint) {  
      String name = joinPoint.getSignature().getName();  
      LOGGER.info("后置通知logEnd==>{}===【args:{}】", name, Arrays.asList(joinPoint.getArgs()));  
   }  
  
   /**  
    * 异常通知  
    *  
    * @param joinPoint 封装了 AOP 中切面方法的信息  
    * @param e         异常  
    */  
   @AfterThrowing(value = "pointcut()", throwing = "e")  
   public void logError(JoinPoint joinPoint, Exception e) {  
      String name = joinPoint.getSignature().getName();  
      LOGGER.info("异常通知logError==>{}===【args:{}】【exception: {}】", name, Arrays.asList(joinPoint.getArgs()), e.getMessage());  
   }  
}
```

创建一个业务类：`HelloService`：

```java
@Service  
public class HelloService {  
   private static final Logger LOGGER = LoggerFactory.getLogger(HelloService.class);  
  
   public HelloService() {  
      LOGGER.info("...HelloService创建了...");  
   }  
  
   /**  
    * 切面目标方法  
    */  
   public String sayHello(String name) {  
      LOGGER.info("目标方法执行：你好，{}", name);  
  
      // 模拟异常  
//     Object o1 = new ArrayList<>(10).get(11);  
  
      return "你好，返回通知";  
   }  
}
```

创建一个配置类 `MainConfig`：

```java
@Configuration  
@EnableAspectJAutoProxy  
@ComponentScan({"top.xiaorang.aop"})  
public class MainConfig {  
}
```

最后创建一个测试类 `SpringAopSourceTests`：

```java
public class SpringAopSourceTests {  
   private static final Logger LOGGER = LoggerFactory.getLogger(SpringAopSourceTests.class);  
  
   public static void main(String[] args) {  
      ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);  
      HelloService helloService = applicationContext.getBean(HelloService.class);  
      LOGGER.info("======================华丽的分割线=========================");  
      helloService.sayHello("小让");  
      LOGGER.info("======================华丽的分割线=========================");  
   }  
}
```

测试结果如下所示：  
![](attachments/Pasted%20image%2020220918164857.png)  

### 3.2. SpringMVC 简单案例

选中项目右键新建一个模块，选择 Gradle，点击下一步，模块名填自己喜欢的即可，这里我就填 `spring-webmvc-study`，最后点击确定即可。  
![|600](attachments/Pasted%20image%2020220918165500.png)  
模块建好之后，在模块的 `build.gradle` 文件中引入以下依赖：

```gradle
dependencies {  
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'  
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'
    // 引入 Spring-mvc
    implementation(project(":spring-webmvc"))  
	// 引入 servlet-api
	compileOnly group: 'javax.servlet', name: 'javax.servlet-api', version: '4.0.1'
}
```

💡需要特别注意的一个点：除了在 `build.gradle` 文件中引入依赖之外，还得在 plugins 选项中增加 `id: 'war'`，否则使用 Tomcat 启动时会一直报错！这个地方摸索了一天，在此提出来，希望小伙伴们可以少走弯路。如下所示：  
![|800](attachments/Pasted%20image%2020220918170444.png)  
配置项目结构：  
![](attachments/Pasted%20image%2020220918171436.png)  
开始编写测试代码，在 webapp 目录下新建 `index.jsp` 页面。

```jsp
<%--  
  Created by IntelliJ IDEA.  User: liulei  Date: 2022/9/18  Time: 17:12  To change this template use File | Settings | File Templates.--%>  
<%@ page contentType="text/html;charset=UTF-8" language="java" %>  
<html>  
<head>  
    <title>Title</title>  
</head>  
<body>  
  
</body>  
</html>
```

两个配置类：`SpringConfig` 和 `SpringMVCConfig`

```java
@ComponentScan(value = "top.xiaorang.mvc", excludeFilters = {  
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)  
})  
public class MySpringConfig {  
    // 这个 Spring 的父容器  
}
```

```java
@ComponentScan(value = "top.xiaorang.mvc", includeFilters = {  
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)  
}, useDefaultFilters = false)  
public class MySpringMVCConfig {  
}
```

一个简单的 `Controller`：

```java
@RestController  
@RequestMapping("/hello")  
public class HelloController {  
    @GetMapping  
    public String hello() {  
        return "Hello SpringMVC !";  
    }  
}
```

最后，编写一个启动类：

```java
public class AppQuickStater extends AbstractAnnotationConfigDispatcherServletInitializer {  
    /**  
     * 获取根容器的配置（Spring 的配置文件 ===> Spring 的配置类 ===> MySpringConfig.class）  
     */  
    @Override  
    protected Class<?>[] getRootConfigClasses() {  
        return new Class<?>[]{MySpringConfig.class};  
    }  
  
    /**  
     * 获取 Web 容器的配置（SpringMVC 的配置文件 ===> SpringMVC 的配置类 ===> MySpringMVCConfig.class）  
     */  
    @Override  
    protected Class<?>[] getServletConfigClasses() {  
        return new Class<?>[]{MySpringMVCConfig.class};  
    }  
  
    /**  
     * Servlet 的映射,DispatcherServlet 的映射路径  
     */  
    @Override  
    protected String[] getServletMappings() {  
        return new String[]{"/"};  
    }  
}
```

配置 Tomcat：  
![|800](attachments/Pasted%20image%2020220918172518.png)  
![|800](attachments/Pasted%20image%2020220918172244.png)  
配置完成之后，就可以启动 Tomcat 看看效果。  
![](attachments/Pasted%20image%2020220918172808.png)  
浏览器访问 [localhost:8080/mvc/hello](http://localhost:8080/mvc/hello)，发现成功输出 `Hello SpringMVC !`。至此，Spring 源码环境搭建成功！🥳🥳🥳  
