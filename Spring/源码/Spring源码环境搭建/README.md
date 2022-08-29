---
title: Spring源码环境搭建
tags: spring 源码
created: 2022-08-30 02:01:56
modified: 2022-08-30 03:40:21
---

## 源码下载

spring 源码地址为：[https://github.com/spring-projects/spring-framework](https://github.com/spring-projects/spring-framework)  
可以直接使用 `git clone https://github.com/spring-projects/spring-framework.git` 命令下载或者直接下载压缩包。  
由于某些原因，可能有的小伙伴下载起来会非常的慢！所以，建议先将项目从 Github 中导入到 Gitee 中，然后直接从 Gitee 中下载，你会发现下载速度非常快！🚀🚀🚀  
![](attachements/Pasted%20image%2020220830022514.png)  
![|800](attachements/Pasted%20image%2020220830022717.png)  
本人是先选择到 5.3.21 版本，然后 **直接下载的 zip 压缩包**。  
![|300](attachements/Pasted%20image%2020220830023643.png)  
当然，小伙伴也可以按照自己喜欢通过 git 命令 `git clone https://gitee.com/liulei0713/spring-framework/tree/v5.3.21/` 下载。

## 配置

本人使用的 IDEA 版本是 2022 版，使用 IDEA 导入下载好的 Spring 项目，默认会开始编译，此时先停止编译。  
![](attachements/Pasted%20image%2020220830024127.png)  
Spring 源码使用 gradle 来构建编译，在编译过程中需要下载一堆的插件和 jar 包，众所周知，下载的资源都是从国外下载，如果不使用国内源来下载，怕是编译时黄花菜都凉了，所以在这里得先配置将源换到国内的源，阿里云仓库：[https://developer.aliyun.com/mvn/guide?spm=a2c6h.13651104.0.0.435836a4x5Dhns](https://developer.aliyun.com/mvn/guide?spm=a2c6h.13651104.0.0.435836a4x5Dhns).  
![](attachements/Pasted%20image%2020220830024553.png)

```gradle
maven { url "https://maven.aliyun.com/repository/public" }  
maven { url "https://maven.aliyun.com/repository/gradle-plugin" } 
```

找到项目根目录下的 `build.gradle` 和 `settings.gradle` 文件中的 `repositories` 关键字所在的地方，复制上面的内容粘贴到此处，如：settings.gradle

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

build.gradle：

```gradle
repositories {  
   maven { url "https://maven.aliyun.com/repository/public" }  
   maven { url "https://maven.aliyun.com/repository/gradle-plugin" }  
   mavenCentral()  
   maven { url "https://repo.spring.io/libs-spring-framework-build" }  
}
```

`build.gradle` 和 `settings.gradle` 文件配置好后，开始配置 gradle，将 IDEA 中的 gradle 指定到自己下载的 gradle，配置步骤如下所示：  
![|600](attachements/Pasted%20image%2020220830025606.png)<br />  
![](attachements/Pasted%20image%2020220830025843.png)  
配置好 gradle 路径之后，开始编译。编译的时间长短可能与小伙伴的网速有关，因为要下载大量的 jar 包和插件，不过咱们配置了阿里云镜像，再慢也不会慢到哪里去 ，静静等待即可。等出现 BUILD SUCCESSFUL 字样就表示已经编程成功。  
![](attachements/Pasted%20image%2020220830030432.png)  
编译成功之后，使用 gradle 测试一下。  
![|500](attachements/Pasted%20image%2020220830031521.png)  
双击点击执行，在执行过程中发现报错，其实是因为 `isAccessible()` 方法被弃用了，咱们把这个方法改成 `canAccess(null)` 方法。  
![](attachements/Pasted%20image%2020220830031806.png)  
再测试一下，发现执行成功！最后会提示 'git' 相关错误，但是不影响使用。  
![](attachements/Pasted%20image%2020220830032133.png)  
上面关于 git 的错误意思是当前不是一个 git 仓库。这个好办，咱们直接使用 `git init` 命令建一个 git 仓库就好，然后再使用 `git add .` 将文件添加到暂存区，最后使用 `git commit -m "fix:  git command error"` 提交到仓库，有需要的小伙伴还可以在 Github 或者 Gitee 建立一个远程仓库，然后将代码推送到远程仓库中。

## 测试

选中项目右键新建一个模块，选择 Gradle，点击下一步，模块名填自己喜欢的即可，这里我就填 `spring-aop-study`，最后点击确定即可。  
![|800](attachements/Pasted%20image%2020220830032938.png)<br />  
模块建好之后，在模块的 `build.gradle` 文件中引入 `spring-context` 依赖。

```gradle
dependencies {  
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'  
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'
    implementation(project(':spring-context'))
}
```

开始编写测试代码，先创建一个实体类 `Person`：

```java
public class Person {  
   private String name;  
   private Integer age;  
  
   public Person() {  
   }  
   public Person(String name, Integer age) {  
      this.name = name;  
      this.age = age;  
   }  
  
   public String getName() {  
      return name;  
   }  
  
   public void setName(String name) {  
      this.name = name;  
   }  
  
   public Integer getAge() {  
      return age;  
   }  
  
   public void setAge(Integer age) {  
      this.age = age;  
   }  
  
   @Override  
   public String toString() {  
      return "Person{" +  
            "name='" + name + '\'' +  
            ", age=" + age +  
            '}';  
   }  
}
```

创建一个配置类 `MainConfig`：

```java
@Configuration  
public class MainConfig {  
   @Bean  
   public Person person(){  
      return new Person("小让", 27);  
   }  
}
```

最后在 `src\test\java` 目录下创建一个测试类 `SpringAopSourceTests`：

```java
public class SpringAopSourceTests {  
   @Test  
   public void test() {  
      ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);  
      Person person = applicationContext.getBean(Person.class);  
      System.out.println(person);  
   }  
}
```

测试结果如下所示：控制台打印 `Person{name=' 小让', age=27}`  
![](attachements/Pasted%20image%2020220830033831.png)  
至此，Spring 源码环境搭建成功！🥳🥳🥳
