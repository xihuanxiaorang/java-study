---
title: Springboot 源码构建
tags: java 源码 springboot
created: 2022-05-06 14:18:45
modified: 2022-05-07 02:29:49
---

# 1、下载 springboot 源码

使用 git 命令下载 2.6.x 版本的 springboot 源码
git clone -b 2.6.x https://github.com/xihuanxiaorang/spring-boot.git
```ad-tip
如果速度比较慢的小伙伴可以通过gitee中转一下，**先将github中的项目导入到gitee中，然后我们再从gitee中拉取项目**
```

# 2、导入 IDEA

打开 IDEA，找到 File -> Open -> 选择你刚才下载的 springboot 源码文件夹，将项目导入到 IDEA 中。
![[Pasted image 20220506232710.png | 600]]

# 3、配置 IDEA

打开 Project Structure，修改 Project 的 SDK 为 1.8 版本。
![[Pasted image 20220506232900.png | 1000]]
打开 Settings，修改 Gradle 配置
![[Pasted image 20220506233149.png | 1000]]

# 4、编译前的优化

因为 springboot 2.2.9 版本之后的源码使用 gradle 来构建，在编译过程中需要下载一堆的插件和 jar 包，众所周知，下载的资源都是从国外下载，如果不使用国内源来下载，怕是编译时黄花菜都凉了，所以在这里我们得配置将源换到国内的源，阿里云仓库： [https://developer.aliyun.com/mvn/guide?spm=a2c6h.13651104.0.0.435836a4x5Dhns](https://developer.aliyun.com/mvn/guide?spm=a2c6h.13651104.0.0.435836a4x5Dhns) .
![[image.png]]
在 build.gradle 和 settings.gradle 文件中的 repositories 关键字所在的地方，加上阿里云镜像配置：
```gradle
maven { url "https://maven.aliyun.com/repository/public" }
maven { url "https://maven.aliyun.com/repository/gradle-plugin" }
```
1. 找到项目根目录下 buildSrc 目录下的 build.gradle 文件中的 repositories 关键字所在的地方，加上阿里云镜像配置
![[Pasted image 20220506234626.png | 600]]
2. 找到项目根目录下 buildSrc 目录下的 settings.gradle 文件中的 repositories 关键字所在的地方，加上阿里云镜像配置
![[Pasted image 20220506234723.png | 800]]
3. 找到项目根目录下的 build.gradle 文件中的 repositories 关键字所在的地方，加上阿里云镜像配置。
![[Pasted image 20220506234229.png | 600]]
在该文件的最上方还添加如下配置：
```gradle
buildscript {  
   repositories {  
      maven { url "https://maven.aliyun.com/repository/public" }  
      maven { url "https://maven.aliyun.com/repository/gradle-plugin" }  
   }
}
```
4. 找到项目根目录下的 settings.gradle 文件中的 repositories 关键字所在的地方，加上阿里云镜像配置。
![[Pasted image 20220506234942.png | 600]]
点击 Gradle 的刷新按钮，发现右下角已经在执行 Gradle 的 Build 操作的同时会下载大量的 jar 包，耐心等待编译完成即可。

# 5、冒烟测试

找到 spring-boot-tests -> spring-boot-smoke-tests -> spring-boot-smoke-test-web-freemarker 测试模块
![[Pasted image 20220506235921.png | 800]]
找到 Springboot 项目的启动类，点击 main 方法左侧的运行按钮，将 Springboot 测试项目运行起来
```java
@SpringBootApplication  
public class SampleWebFreeMarkerApplication {  
  
   public static void main(String[] args) {  
      SpringApplication.run(SampleWebFreeMarkerApplication.class, args);  
   }  
  
}
```
可以看到 Springboot 项目已经成功启动，是不是和咱们平时写的 Springboot 应用没什么区别！
![[Pasted image 20220507000430.png]]
这个测试模块还写了一个 Controller
```java
@Controller  
public class WelcomeController {  
  
   @Value("${application.message:Hello World}")  
   private String message = "Hello World";  
  
   @GetMapping("/")  
   public String welcome(Map<String, Object> model) {  
      model.put("time", new Date());  
      model.put("message", this.message);  
      return "welcome";  
   }  
  
}
```
发起请求 http://localhost:8080/ ，发现浏览器页面打印
![[Pasted image 20220507000855.png | 300]]
至此，咱们的Springboot源码阅读环境就已经搭建完成🎉🎉🎉
