---
title: SpringMvc
tags: java springmvc
created: 2022-04-17 10:51:38
modified: 2022-08-23 15:45:09
---

本章节所涉及到的代码在 [GitHub - xihuanxiaorang/spring-mvc-study: 学习SpringMvc之用(已完结)](https://github.com/xihuanxiaorang/spring-mvc-study) 仓库中，可以自行查看。

# 1、环境搭建

## 1、Servlet 配置文件 - Web. Xml

```xml
<?xml version="1.0" encoding="UTF-8"?>  
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
         xmlns="http://xmlns.jcp.org/xml/ns/javaee"  
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"  
         version="4.0">  
  
    <servlet>  
        <servlet-name>dispatcherServlet</servlet-name>  
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>  
        <!--        指定SpringMvc配置文件的路径-->  
        <init-param>  
            <param-name> contextConfigLocation </param-name>  
            <param-value>classpath:dispatcher.xml</param-value>  
        </init-param>  
        <!--        本Servlet会在Tomcat启动的时候就会被创建-->  
        <load-on-startup>1</load-on-startup>  
    </servlet>  
    <servlet-mapping>  
        <servlet-name>dispatcherServlet</servlet-name>  
        <url-pattern>/</url-pattern>  
    </servlet-mapping>  
</web-app>
```

## 2、SpringMvc 核心配置文件 - dispatcher. Xml

也是 Spring 的配置文件，放置路径可以根据需要随意放置，但是一般放在资源文件夹的根下。  
如何创建 Spring 配置文件：右键点击 resources 资源文件夹，点击 New -> XML Configuration File -> Spring Config。，取名 `dispatcher.xml` 。  
![[Pasted image 20220417155233.png]]

```xml
<?xml version="1.0" encoding="UTF-8"?>  
<beans xmlns="http://www.springframework.org/schema/beans"  
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
       xmlns:context="http://www.springframework.org/schema/context"  
       xmlns:mvc="http://www.springframework.org/schema/mvc"  
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd">  
  
    <!--    设置注解扫描路径-->  
    <context:component-scan base-package="top.xiaorang"/>  
  
    <!--    引入SpringMvc的核心功能-->  
    <mvc:annotation-driven/>  
</beans>
```

其中，`mvc: annotation-driven` 主要引入了 2 个核心的类：

1. RequestMappingHandlerMapping：实现了 HandlerMapping 接口，它会处理@RequestMapping 注解，将其注册到请求映射表中
2. RequestMappingHandlerAdapter：实现了 HandlerAdapter 接口，它是处理请求的适配器，确定调用某个符合要求的控制器类中具体的服务方法

## 3、Maven 配置文件 - pom. Xml

主要用于添加项目依赖

```xml
<dependency>  
    <groupId>javax.servlet</groupId>  
    <artifactId>javax.servlet-api</artifactId>  
    <version>3.1.0</version>  
    <scope>provided</scope>  
</dependency>  
  
<dependency>  
    <groupId>javax.servlet</groupId>  
    <artifactId> jstl </artifactId>  
    <version> 1.2 </version>  
</dependency>  
  
<dependency>  
    <groupId>javax.servlet.jsp</groupId>  
    <artifactId>javax.servlet.jsp-api</artifactId>  
    <version>2.3.1</version>  
    <scope> provided </scope>  
</dependency>  
  
<dependency>  
    <groupId>org.springframework</groupId>  
    <artifactId>spring-web</artifactId>  
    <version>5.1.14.RELEASE</version>  
</dependency>  
  
<dependency>  
    <groupId>org.springframework</groupId>  
    <artifactId>spring-webmvc</artifactId>  
    <version>5.1.14.RELEASE</version>  
</dependency>  
  
<dependency>  
    <groupId>org.springframework</groupId>  
    <artifactId>spring-tx</artifactId>  
    <version>5.1.14.RELEASE</version>  
</dependency>  
  
<dependency>  
    <groupId>org.springframework</groupId>  
    <artifactId>spring-jdbc</artifactId>  
    <version>5.1.14.RELEASE</version>  
</dependency>  
  
<dependency>  
    <groupId>org.mybatis</groupId>  
    <artifactId>mybatis-spring</artifactId>  
    <version>2.0.2</version>  
</dependency>  
  
<dependency>  
    <groupId>com.alibaba</groupId>  
    <artifactId>druid</artifactId>  
    <version>1.1.18</version>  
</dependency>  
  
<dependency>  
    <groupId>mysql</groupId>  
    <artifactId>mysql-connector-java</artifactId>  
    <version>8.0.28</version> 
</dependency>  
  
<dependency>  
    <groupId>org.mybatis</groupId>  
    <artifactId>mybatis</artifactId>  
    <version>3.4.6</version>  
</dependency>  
  
<dependency>  
    <groupId>junit</groupId>  
    <artifactId>junit</artifactId>  
    <version>4.11</version>  
    <scope>test</scope>  
</dependency>  
  
<dependency>  
    <groupId>org.springframework</groupId>  
    <artifactId>spring-context</artifactId>  
    <version>5.1.4.RELEASE</version>  
</dependency>  
  
<dependency>  
    <groupId>org.springframework</groupId>  
    <artifactId>spring-aop</artifactId>  
    <version>5.1.14.RELEASE</version>  
</dependency>  
  
<dependency>  
    <groupId>org.aspectj</groupId>  
    <artifactId>aspectjrt</artifactId>  
    <version>1.8.8</version>  
</dependency>  
  
<dependency>  
    <groupId>org.aspectj</groupId>  
    <artifactId>aspectjweaver</artifactId>  
    <version>1.8.3</version>  
</dependency>  
  
<dependency>  
    <groupId>org.slf4j</groupId>  
    <artifactId>slf4j-log4j12</artifactId>  
    <version>1.7.25</version>  
</dependency>  
  
<dependency>  
    <groupId>log4j</groupId>  
    <artifactId>log4j</artifactId>  
    <version>1.2.17</version>  
</dependency>
```

配置 idea 在引入依赖时自动下载 jar 包以及构建。  
![[Pasted image 20220417154658.png]]

## 4、配置 Tomcat

TODO...

# 2、第一个 SpringMvc 程序

## 1、编码开发

控制器的主要功能：

```markdown
1. 接收客户端请求参数
2. 调用Service处理业务功能
3. 页面跳转
```

以前的 Servlet 程序：

```java
public class HelloServlet extends HttpServlet {  
    @Override  
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {  
    }  
}
```

现在的 SpringMvc 中的控制器程序：

```java
@Controller  
public class HelloController {  
    @RequestMapping("/first")  
    public String first(HttpServletRequest request, HttpServletResponse response) {  
        System.out.println("HelloController#first");  
        return "result.jsp";  
    }  
  
    @RequestMapping("/second")  
    public String second(HttpServletRequest request, HttpServletResponse response) {  
        System.out.println("HelloController#second");  
        return "result.jsp";  
    }  
}
```

使用 Servelt 时的缺点：

1. 接收客户端请求参数时只能接收字符串类型的数据，需要手动进行类型转换；而且无法自动封装成对象
2. 调用 Service 业务代码时，只能 new 出来 Service 对象，造成代码耦合
3. 当 jsp 文件移动位置或者视图层不再使用 jsp 的时候，需要去修改原来的代码，存在跳转路径以及视图层技术上的耦合

Servlet 中存在的缺点，在 SpringMvc 中得以解决，这也就间接证明为什么要使用 SpringMvc。

## 2、细节分析

### 1、一种类型的 SpringMvc 控制器被创建几次？

回顾：一种类型的 Servlet 只会被 Tomcat 创建一次，所以 Servlet 是单实例的。单实例并不是单例设计模式 (构造器私有)！  
SpringMvc 的控制器可以被 Spring 创建一次或者多次，默认情况下只会被创建一次 (当控制器类中有线程不安全的成员变量时，可能会存在线程安全的问题)。可以使用@Scope("prototype") 注解来修改创建次数。

### 2、@RequestMapping 注解

核心作用：为控制器方法提供外部访问的 url 路径。

- 路径分隔符 "/" 可以省略，注意：多级目录只能开头可以不写 "/"

```java
@RequestMapping("xiaorang/second")  
public String second(HttpServletRequest request, HttpServletResponse response) {  
    System.out.println("HelloController#second");  
    return "/result.jsp";  
}
```

- 在一个控制器方法上映射多个路径

```java
@RequestMapping(value = {"/first", "third"})  
public String first(HttpServletRequest request, HttpServletResponse response) {  
    System.out.println("HelloController#first");  
    return "/result.jsp";  
}
```

- 在 Controller 类上加@RequestMapping 注解，访问时还得加上类上的路径 => `/test/xiaorang/second` 。更好的按照功能，进行不同模块的区分，有利于项目的管理。

```java
@Controller  
@RequestMapping("user")  
public class UserController {  
    @RequestMapping(value = "/add")  
    public String add(HttpServletRequest request, HttpServletResponse response) {  
        System.out.println("UserController#add");  
        return "/result.jsp";  
    }  
  
    @RequestMapping("query")  
    public String query(HttpServletRequest request, HttpServletResponse response) {  
        System.out.println("UserController#query");  
        return "/result.jsp";  
    }  
}
```

- @RequestMapping 限定用户的请求方式 (Web 开发中的 POST 请求和 Get 请求)
	- Get 请求：通过请求行 (地址栏) 提交数据，明文数据提交，不安全，提交的数据量小 (不能超过 2048 字节)，如 `http://localhost:8500/basic/user/add?username=admin&password=123456`
	- POST 请求：通过请求体提交数据，密文提交 (不是加密，指的是一般用户不可见)，相对安全，提交数据量大 (理论上没有限制)

默认情况下，@RequestMapping 注解接收所有请求方式的访问 (GET、POST、PUT、DELETE...)。  
那么如何限定某个控制器方法只接收某个特定请求方式的请求呢🤔？在@RequestMapping 注解中添加 method 属性，指定一个或多个类型的请求方式即可。

```java
@RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.PUT})  
public String add(HttpServletRequest request, HttpServletResponse response) {  
    System.out.println("UserController#add");  
    return "/result.jsp";  
}
```

当用户发起了@RequestMapping 注解不支持的请求操作时，SpringMvc 会在服务器端抛出一个 405 错误，Method Not Allowed！  
除 POST、GET 这两种请求之外，其他的请求方式浏览器支持的不好，但是可以使用专属工具 (POSTMAN) 或者库 (AJAX) 进行测试。  
其他的请求方式，大多数不支持响应视图技术 (JSP、Thymeleaf)，只能返回简单字符串或者 JSON 数据，其实现在在前后端分离的项目中都是返回的 SON 数据。如果想返回 JSON 数据，需要在控制器类或者方法上另加上 **@ResponseBody** 注解。

### 3、控制器方法参数

SpringMvc 在控制器方法参数设计的过程中，非常灵活，可以设置多种参数的设置方式，非常强大，也把这设计叫做 **数据绑定**。  
在 Servlet 开发中，在重写 service 方法时，方法参数必须要有 HttpServletRequest 和 HttpServletResponse。但是现在使用 SpringMvc 开发时，这些方法参数 (HttpServletRequest、HttpServletResponse、HttpSession) 可有可无，可以任意组合。  
在实际开发中其实很少用到 Servlet API，一般都是 **利用 SpringMvc 将请求参数封装到 POJO 对象** 中，方便后续业务开发使用！  
💡需要注意的是，**ServletContext 不能作为在控制器方法的形参**，只能在代码中通过 `sessionContext.getServletContext` 获取。

### 4、视图解析器 (页面跳转)

🔥目前存在的问题：控制器中的跳转路径与实际视图路径存在耦合。当 jsp 文件的位置移动时，需要修改原有代码。  
那么如何解决上述的问题呢？这个时候 SpringMvc 中的 **视图解析器** 就可以很好的解决上述问题。  
如何实现呢？xml 版本配置：

1. 在 SpringMvc 核心配置文件 `dispatcher.xml` 文件中，注册一个视图解析器的 Bean，配置视图解析器中的两个属性，前缀 prefix = "/"，后缀 suffix = ".jsp"。

```xml
<!--    配置视图解析器-->  
<bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">  
    <!--        路径-->  
    <property name="prefix" value="/"/>  
    <!--        文件类型-->  
    <property name="suffix" value=".jsp"/>  
</bean>
```

2. 修改原来控制器 controller 中的方法，只返回 jsp 文件名即可，去掉前缀 "/" 和 后缀 ".jsp"。在实际处理时，SpringMvc 就会把视图解析器与控制器方法中的逻辑视图名进行拼接。

```java
@RequestMapping(value = "query", method = RequestMethod.GET)  
public String query(HttpServletRequest request, HttpServletResponse response) {  
    System.out.println("UserController#query");  
    return "result";  
}
```

注解版本配置：

```java
@Configuration  
public class AppConfig {  
    @Bean  
    public ViewResolver viewResolver() {  
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();  
        viewResolver.setPrefix("/");  
        viewResolver.setSuffix(".jsp");  
        return viewResolver;  
    }  
}
```

💡需要注意的是，AppConfig 类需要在包扫描路径下才能生效。

### 5、SpringMvc 配置文件的默认设置

如果在 Web.xml 配置文件中没有显式的配置 SpringMvc 配置文件所在路径，系统会查找默认的配置文件，/WEB-INF/[servlet-name]-servlet.xml。根据上述配置，SpringMvc 配置文件的默认名字为 dispatcherServlet-servlet.xml，放置在 /WEB-INF 目录下。  
📍如果没有显式的指定 SpringMvc 配置文件所在路径，默认的配置文件又不存在，那么在启动 Tomcat 的时候就会抛出异常！  
![[Pasted image 20220417221318.png]]

# 3、SpringMvc 控制器开发详解 (一)

## 1、核心要点

```ad-important
1. 接收客户端请求参数[讲解ing]
2. 调用业务对象
3. 页面跳转
```

## 2、控制器接收客户端请求参数

### 1、基于 Servlet API 接收客户端请求参数

```java
@RequestMapping(value = "/param1")  
public String param1(HttpServletRequest request, HttpServletResponse response) {  
    String username = request.getParameter("username");  
    String password = request.getParameter("password");  
    System.out.println("username：" + username + "\r\n" + "password：" + password);  
    return "result";  
}
```

浏览器地址栏访问 <http://localhost:8500/basic/param/param1?username=admin&password=123456>  
控制台会打印出 username: admin password:123456  
这种方式虽然最为直观、简单、但是代码冗余并且与 Servlet API 存在耦合，所以在 SpringMvc 开发中，这种接收客户端请求参数的方式并不建议使用。  
那么在 SpringMvc 中是如何做的呢？别急，答案就在下面！

### 2、基于简单变量接收客户端请求参数

所谓的 **简单变量**，指的是 8 种基本类型 +String 类型的变量。把这些类型的变量，直接作为控制器方法的形参，用于接收客户端提交的数据。

```java
@RequestMapping(value = "/param2")  
public String param2(String username, Integer age) {  
    System.out.println("username：" + username + "\r\n" + "age：" + age);  
    return "result";  
}
```

浏览器地址栏访问 <http://localhost:8500/basic/param/param2?username=admin&age=10>  
控制台也会打印出 username: admin age:10

- 🎨细节分析
	- 常见类型自动转换：
		- Http 协议传递的数据是 name="admin" 和 age="10"，代码中的 age 形参类型是 Integer 类型，还是可以赋值成功。这是为什么呢？因为 SpringMvc 底层针对 age 接收数据时会自动调用 Integer.parseInt() 方法尝试对传递的数据进行类型转换。
		- 常见类型泛指 8 种基本类型及其包装类 +String 类型。像 Date 日期等特殊类型，默认是不支持的，需要自定义类型转换器。
	- 基本类型尽量使用对应的包装类：
		- 比如说控制器方法中有一个 int 类型的 age 形参，但是请求中又不存在这个参数，如 <http://localhost:8500/basic/param/param2?username=admin> ，等同于 null，而 int 类型，是基本类型，无法存储 nulL 值，所以就会报错。
		- 那么如何解决呢？①将 age 参数使用包装类 Integer，Integer 是可以存储 null 值的；②或者为 age 参数设置默认值，但是需要@RequestParam 注解配合使用。

### 3、基于 POJO 类型接收客户端请求参数

POJO：Plain Ordinary Java Object，简单的 Java 对象  
POJO 类型的特点：

1. 类中的成员变量必须提供 setter、getter 方法
2. 必须提供无参构造方法
3. 可以实现 Serializable，也可以不实现
4. 不实现容器或者框架所规定的接口

其实，在项目中根据业务封装的实体、DTO、VO 这些类型就是 POJO。  
编码开发：

1. 编写一个 POJO

```java
public class User {  
    private String username;  
    private String password;  
    private Integer age;  
  
    public String getUsername() {  
        return username;  
    }  
  
    public void setUsername(String username) {  
        this.username = username;  
    }  
  
    public String getPassword() {  
        return password;  
    }  
  
    public void setPassword(String password) {  
        this.password = password;  
    }  
  
    public Integer getAge() {  
        return age;  
    }  
  
    public void setAge(Integer age) {  
        this.age = age;  
    }  
  
    @Override  
    public String toString() {  
        return "User{" +  
                "username='" + username + '\'' +  
                ", password='" + password + '\'' +  
                ", age=" + age +  
                '}';  
    }  
}
```

2. 编写一个控制器方法

```java
@RequestMapping(value = "/register")  
public String register(User user) {  
    System.out.println(user);  
    return "result";  
}
```

3. 发起请求访问

访问 <http://localhost:8500/basic/user/register?username=xiaorang&password=123456&age=26>  
控制台打印 User{username='xiaorang', password='123456', age=26}  
💡值得注意的是：

1. 如果 SpringMvc 发现控制器方法形参类型是 8 种基本类型以及包装类型 +String 类型的话，**它会通过形参名与请求参数 Key 对应**，来接收数据
2. 如果 SpringMvc 发现控制器方法形参类型不是 8 种基本类型以及包装类型 +String 类型的话，即 POJO 类型，它会查找对应 **形参类型的属性名与请求参数的 key 对应**，来接收数据，说白了，就是 POJO 中成员变量的名字需要和请求参数中的 key 或者表单 name 属性的值严格对应，否则获取不到值！
3. 如果 **存在自定义类型转换器** 的话，上述规律就不适用了，以转换器为准。

### 4、接收一组简单变量的请求参数

```java
@RequestMapping(value = "/param3")  
public String param3(int[] ids) {  
    System.out.println(Arrays.toString(ids));  
    return "result";  
}
```

发送请求 <http://127.0.0.1:8500/basic/param/param3?ids=1&ids=2&ids=3>  
控制台打印 [1,2, 3]  
细节分析：

- 当使用 List 集合来接收一组简单变量的请求参数时，会抛出异常！说找不到 List 接口的默认无参构造方法

```java
@RequestMapping(value = "/param3")  
public String param3(List<Integer> ids) {  
    System.out.println(Arrays.toString(ids));  
    return "result";  
}
```

![[Pasted image 20220418150705.png]]

- 上面竟然报错，那么试一下 List 的实现类 ArrayList 呢？

```java
@RequestMapping(value = "/param3")  
public String param3(ArrayList<Integer> ids) {  
    System.out.println(ids);  
    return "result";  
}
```

发现没报错，但是控制台打印 []，并没有接收到值。  
为什么呢？因为 SpringMvc 会按照 POJO 的匹配方式，对形参的数据类型中的成员变量进行查找，ArrayList 类中怎么可能会有 ids 属性，所以匹配不上，接收不到值。

### 5、接收一组 POJO 类型的请求参数

1. 封装一个 DTO 类 (其实也是一个 POJO)，将一组 POJO 类型的请求参数作为其属性，注意属性名要和请求中请求参数的 key 相同

```java
public class UserDto {  
    private List<User> users;  
  
    public List<User> getUsers() {  
        return users;  
    }  
  
    public void setUsers(List<User> users) {  
        this.users = users;  
    }  
  
    @Override  
    public String toString() {  
        return "UserDto{" +  
                "users=" + users +  
                '}';  
    }  
}
```

2. 将 DTO 作为控制器方法的形参类型

```java
@RequestMapping(value = "/param4")  
public String param4(UserDto userDto) {  
    System.out.println(userDto);  
    return "result";  
}
```

3. 编写 jsp 页面

```html
<%@ page contentType="text/html;charset=UTF-8" %>  
<html>  
<head>  
    <title>接收一组POJO类型的请求参数</title>  
</head>  
<body>  
<form action="${pageContext.request.contextPath}/param/param4" method="post">  
    userName1:<input type="text" name="users[0].username"/> <br/>  
    password1:<input type="text" name="users[0].password"/> <br/>  
    age1:<input type="text" name="users[0].age"/> <br/>  
    <hr/>  
    userName2:<input type="text" name="users[1].username"/><br/>  
    password2:<input type="text" name="users[1].password"/> <br/>  
    age2:<input type="text" name="users[1].age"/> <br/>  
  
    <input type="submit" value="sumbit"/>  
</form>  
</body>  
</html>
```

4. 发送请求 <http://localhost:8500/basic/param4.jsp> ，填写完值，点击提交即可

没有报错，控制台打印 UserDto{users=[User{username='xiaorang', password='123456', age=28}, User{username='admin', password='123456', age=18}]}

## 3、@RequestParam 注解

用于修饰控制器方法中的形参，如

```java
@RequestMapping(value = "/param2")  
public String param2(@RequestParam String username, @RequestParam Integer age) {  
    System.out.println("username：" + username + "\r\n" + "age：" + age);  
    return "result";  
}
```

### 1、解决请求参数与方法形参名字不一致的问题

当发送的请求是 <http://localhost:8500/basic/param/param2?n=admin&a=10> 时，如果还是以前的写法则会抛出 400 异常，说字符串类型的参数 username 不存在！  
![[Pasted image 20220418165456.png]]  
此时就需要用到@RequestParam 注解的第一个作用，解决请求参数与形参名字不一致的问题，那么怎么做呢🤔？让@RequestParam 注解的 value 属性等于请求参数的 key 即可。

```java
@RequestMapping(value = "/param2")  
public String param2(@RequestParam("n") String username, @RequestParam("a") Integer age) {  
    System.out.println("username：" + username + "\r\n" + "age：" + age);  
    return "result";  
}
```

此时再发送请求 <http://localhost:8500/basic/param/param2?n=admin&a=10> ，发现没有报错，控制台打印 username 和 age，并且页面发生跳转！🥳  
🎯注意事项：

- @RequestParam 注解简写形式：不书写 value 属性的内容时，@RequestParam 默认会把对应形参名作为 value 属性的值
- 被@RequestParam 注解修饰的形参，客户端必须传递数据，不能省略，否则也会抛出 400 异常，说字符串类型的参数 username 不存在！为什么会这样呢🤔？因为 **@RequestParam 注解中有一个 required 属性，默认为 true，表示必须传递数据给其修饰的形参，否则报错！** 所以我们就可以通过配置@RequestParam 注解中的 required 属性为 false，来使参数不是必须传递就行。
- **@RequestParam 不能用来修饰 POJO 类型的形参**，否则会抛出 400 异常！

### 2、给其修饰的形参赋默认值

从上面的注意事项中知道，当 @RequestParam 注解中的 required 属性为 true 时 (默认为 true)，客户端必须传递数据给其修饰的形参，否则报错！此时如果给 @RequestParam 注解中的另一个属性 defaultValue 赋值，即提供一个默认值给形参，就算客户端没有传递此形参的数据也不会报错，而是会使用提供的默认值。  
💡需要注意的是，如果给 @RequestParam 注解中的 defaultValue 属性赋值，则 required 属性也就默认被设置成了 false ！  
典型的使用场景：分页查询数据时，当是第一页时，不传页号的设计，如

```java
@RequestMapping(value = "query", method = RequestMethod.GET)  
public String query(@RequestParam(defaultValue = "1") Integer pageNum) {  
    System.out.println("pageNum=" + pageNum);  
    return "result";  
}
```

发起请求 <http://localhost:8500/basic/user/query> 控制台打印 pageNum=1  
发起请求 <http://localhost:8500/basic/user/query?pageNum=2> 控制台打印 pageNum=2  
此时，正好说明当请求中没有传递该形参的数据时，形参的值就等于 defaultValue 的值，如果请求中有该形参的数据，则以请求中的数据值为主。

## 4、中文请求参数乱码问题

### 1、乱码问题

当我们访问 <http://localhost:8500/basic/param4.jsp> 时，给页面中的一个 userName 输入中文名字，此时会发现控制台打印出来的名字居然是乱码！  
UserDto{users=[User{username='å°è®©', password='123456', age=28}, User{username='å°æ', password='123456', age=18}]}  
WTF！这是怎么回事😮？居然乱码了！别急，看看 SpringMvc 是怎么解决这个问题的。

### 2、回顾 JavaWeb 开发中中文字符集乱码的解决方案

根据数据提交方式 (请求行、请求体) 不同，分为 GET、POST：

- GET 请求乱码的解决方案

Tomcat8 以前的版本：修改 $TOMCAT_HOME / conf / server.xml 文件，给其中的 Connector 标签加上 URLEncoding="UTF-8" 属性。  
**Tomcat8 版本以及后续版本：Connector 标签默认已经支持 UTF-8 字符集**。

- POST 请求乱码的解决方案

最底层的解决方案：`request.setCharaceterEncoding("UTF-8")` 。  
频繁书写这句代码，不是好的实现，所以经常在开发中通过 **过滤器** 来解决。

```java
public class EncodingFilter implements Filter {  
    @Override  
    public void init(FilterConfig filterConfig) throws ServletException {  
  
    }  
    
    @Override  
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, ServletException {  
        HttpServletRequest request = (HttpServletRequest) servletRequest;  
        request.setCharacterEncoding("UTF-8");  
        filterChain.doFilter(servletRequest, servletResponse);  
    }  
  
    @Override  
    public void destroy() {  
  
    }
}
```

### 3、SpringMvc 解决中文字符集乱码问题

- GET 请求乱码的解决方案

**Tomcat8 以及后续已经在默认使用 UTF-8 字符集，无需我们再处理**。如果你是使用 GBK 字符集或者 Tomcat8 之前的版本的话，那么就需要按照上面的步骤去配置 server.xml 文件。

- POST 请求乱码的解决方案

对于 POST 请求的中文乱码问题，**SpringMvc 已经默认提供了一个过滤器 org.springframework.web.filter.CharacterEncodingFilter 类** 来解决。  
然后在 web.xml 配置文件中添加此过滤器：

```xml
<filter>  
    <filter-name>characterEncodingFilter</filter-name>  
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>  
    <init-param>  
        <param-name>encoding</param-name>  
        <param-value>UTF-8</param-value>  
    </init-param>  
</filter>  
<filter-mapping>  
    <filter-name>characterEncodingFilter</filter-name>  
    <url-pattern>/*</url-pattern>  
</filter-mapping>
```

添加完之后，我们再来测试一下是否已经生效？发起请求  <http://localhost:8500/basic/param4.jsp> ，把 userName 填成中文名字，点击提交，此时发现控制台输出正常了，显示的 userName 是我们填写的中文名字，完美解决！让我们给 SpringMvc 点个赞👍

## 5、SpringMvc 的类型转换器

HTTP 协议，传递的数据都是字符串类型，但是在我们的控制器方法上并不都是用字符串类型的形参来接收，如

```java
@RequestMapping(value = "/param2")  
public String param2(@RequestParam("n") String username, @RequestParam("a") Integer age) {  
    System.out.println("username：" + username + "\r\n" + "age：" + age);  
    return "result";  
}
```

其中的 age 就是 Integer 类型的，我们也没有写 Integer.parseInt("18") 的代码来让字符串类型转换成 Integer 类型，这样类型不匹配不应该报错吗？但是我们发起请求之后，并没有报错，不用想，肯定又是 SpringMvc 帮我们做了类型转换！的确，在 SpringMvc 中提供了内置的类型转换器，用于把客户端提交的字符串类型的请求参数转换成控制器方法参数需要的数据类型。

### 1、SpringMvc 内置的类型转换器

SpringMvc 并没有对所有的类型都提供了内置的类型转换器，它只提供了常见类型的转换器，比如：8 种基本类型 + 常见的集合类型等...  
原理分析：  
在 SpringMvc 启动时，会通过<mvc: annotation-driven/> 标签把 FormattingConversionServiceFactoryBean 引入到 SpringMvc 体系中。FormattingConversionServiceFactoryBean 存储了 SpringMvc 中所有的内置类型转换器。后续客户端提交请求时，发现对应控制器方法的形参不是字符串类型，那么 FormattingConversionServiceFactoryBean 就会调用对应的类型转换器进行类型转换，最终完成控制器方法形参的赋值操作。  
详情见 [[SpringMvc 内置类型转换器注册流程.excalidraw]]

### 2、SpringMvc 中自定义类型转换器

SpringMvc 在接收客户端提交的请求参数时，如果请求参数对应的控制器方法形参是非常规数据类型 (如：Date 类型...)，SpringMvc 默认情况下无法进行类型转换，则会抛出 400 异常！

```java
@RequestMapping(value = "/param5")  
public String param5(String username, Date birthday) {  
    System.out.println(username);  
    System.out.println(birthday);  
    return "result";  
}
```

```html
<%@ page contentType="text/html;charset=UTF-8" %>  
<html>  
<head>  
    <title></title>  
</head>  
<body>  
<form action="${pageContext.request.contextPath}/param/param5" method="post">  
    username:<input type="text" name="username"/> <br/>  
    birthday:<input type="text" name="birthday"/> <br/>  
    <input type="submit" value="submit"/>  
</form>  
</body>  
</html>
```

客户端发送请求 <http://localhost:8500/basic/param5.jsp> ，输入用户名和生日，点击提交  
抛出异常 Failed to convert from type [java.lang.String] to type [java.util.Date] for value '2022-04-18'; nested exception is java.lang.IllegalArgumentException]，无法将字符串类型的数据转换成 Date 类型！  
![[Pasted image 20220418224815.png]]  
那么我们该怎么解决这个问题呢🤔？

1. **自定义日期类型转换器**

```java
public class DateConverter implements Converter<String, Date> {  
    private String pattern = "yyyy/MM/dd";  
  
    @Override  
    public Date convert(String s) {  
        try {  
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);  
            return simpleDateFormat.parse(s);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    public String getPattern() {  
        return pattern;  
    }  
  
    public void setPattern(String pattern) {  
        this.pattern = pattern;  
    }  
}
```

```xml
<bean id="dateConverter" class="top.xiaorang.converter.DateConverter">  
    <property name="pattern" value="yyyy-MM-dd"/>  
</bean>
```

2. **将日期类型转换器注册到 FormattingConversionServiceFactoryBean**

```xml
<bean id="formattingConversionServiceFactoryBean"  
      class="org.springframework.format.support.FormattingConversionServiceFactoryBean">  
    <property name="converters">  
        <set>  
            <ref bean="dateConverter" />  
        </set>  
    </property>  
</bean>
```

3. **替换 SpringMvc 默认创建的 conversionService**

```xml
<mvc:annotation-driven conversion-service="formattingConversionServiceFactoryBean"/>
```

客户端再次发送请求 <http://localhost:8500/basic/param5.jsp> ，输入用户名和生日，点击提交  
此时发现并没有报错并且控制台打印出 小让 Mon Apr 18 00:00:00 CST 2022 ，说明已经将字符串 '2022-04-18' 成功转换成日期类型🥳

## 6、接收其他请求数据

### 1、动态参数收集

#### 1、单值动态参数收集

当请求中请求参数的 key 不一定时，即 key 对于控制器方法的形参来说形参名以及个数不确定，可能会是 name，可能会是 age，那么控制器方法中的形参该定义什么类型来接收呢？Map 集合。

```java
@RequestMapping(value = "/param6")  
public String param6(@RequestParam Map<String, String> params) {  
    System.out.println(params);  
    return "result";  
}
```

发送请求 <http://localhost:8500/basic/param/param6?name=xiaorang> ，控制台打印 {name=xiaorang}  
发送请求 <http://localhost:8500/basic/param/param6?name=xiaorang&age=10> ，控制台打印 {name=xiaorang, age=10}  
![[Pasted image 20220418235857.png]]  
💡需要注意的是，必须使用 @RequestParam 注解修饰，否则接收不到任何值！

#### 2、多值动态参数收集

当客户端发送请求  <http://127.0.0.1:8500/basic/param/param6?ids=1&ids=2&ids=3> 时，发现控制台只打印出来一个值 {ids=1} ，没有达到预期的效果！  
从前面的知识中可以知道控制器方法中的参数如果使用 int 类型的数组，形参名为 ids 的话，像这样 <http://127.0.0.1:8500/basic/param/param3?ids=1&ids=2&ids=3> ，控制台打印 [1,2, 3]，Bingo，就是这样！  
此时如果将请求中请求参数 key 变成了 usernames，但是也想接收到值，结合上面单值动态参数的知识点，大胆猜测一下，也用 Map 集合怎么样？试一下，发送请求 <http://localhost:8500/basic/param/param6?usernames=xiaorang&usernames=xiaoming&usernames=sanshi> ，发现控制台只打印了第一个值 {usernames=xiaorang} ，有值，说明方向没错！但是只有一个，应该是 Map 集合不允许多个 key 值相同，但是 value 不同的值存在。我们将形参类型改成允许多个相同 key 值存在的 Map 类型 -MultiValueMap。

```java
@RequestMapping(value = "/param6")  
public String param6(@RequestParam MultiValueMap<String, String> params) {  
    System.out.println(params);  
    return "result";  
}
```

再次发送请求 <http://localhost:8500/basic/param/param6?usernames=xiaorang&usernames=xiaoming&usernames=sanshi> ，控制台打印 {usernames=[xiaorang, xiaoming, sanshi]}，说明成功接收到值！🥳  
![[Pasted image 20220419002805.png]]

### 2、接收 Cookie 数据

在 Servlet 开发中，使用 `Cookie[] cookies = request.getCookies();` 来获取所有的 Cookie 数据。  
那么在 SpringMvc 中如何简便地获取 Cookie 数据呢？使用 **@CookieValue** 注解。

```java
@RequestMapping(value = "/param7")  
public String param7(@CookieValue("name") String value) {  
    System.out.println(value);  
    return "result";  
}
```

客户端发送请求 <http://localhost:8500/basic/param/param7> ，发送之前添加一个 Cookie，name=xiaorang，发送之后，控制台打印 xiaorang。  
如果浏览器中不好添加 Cookie 的话，可以使用 POSTMAN 或者 Apifox 工具。  
![[Pasted image 20220419004955.png]]  
![[Pasted image 20220419004659.png]]

### 3、接收请求头数据

请求头：HTTP 协议规定，客户端发起请求时，除了提交数据外，还可以通过请求头向服务器提交一些额外的附加信息，比如 语言信息、浏览器的版本、客户端操作系统、是否缓存数据等信息。后续的开发中，在某些特殊场景下，甚至会自定义请求头用来携带一些特殊数据。  
那么如何获取请求头中的数据呢🤔？  
在 Servlet 开发中，使用 `String key = request.getHeader("key");` 来获取请求头中的数据。  
那么在 SpringMvc 中如何获取请求头数据呢？使用 **@RequestHeader** 注解。

```java
@RequestMapping(value = "/param8")  
public String param8(@RequestHeader("Host") String host) {  
    System.out.println(host);  
    return "result";  
}
```

客户端发起请求 <http://localhost:8500/basic/param/param8> ，控制台打印 localhost:8500 。  
![[Pasted image 20220419010115.png]]

# 4、SpringMvc 控制器开发详解 (二)

## 1、核心要点

```ad-important
1. 接收客户端请求参数[讲解完毕]
2. 调用业务对象[讲解ing]
3. 页面跳转
```

## 2、SpringMvc 控制器调用业务对象 (SSM 整合)

### 1、具体步骤

#### 1、新建一个子模块 spring-mvc-study-02

将原来子模块的内容复制拷贝进来，如 **SpringMvc 的核心配置文件 dispatch.xml** 、**Tomcat 核心配置文件 web.xml** 、**日期类型转换器类 DateConverter** 、以及 **日志配置文件 log4j.properties** 和 **首页 index.jsp 文件**。

#### 2、新建一个 tomcat 启动器 ssm

![[Pasted image 20220419111632.png]]  
![[Pasted image 20220419111705.png]]

#### 3、在 dispatcher.xml 增加 Mybatis 与事务需要用到的核心 Bean

在 **SpringMvc 核心配置文件 dispatcher.xml** 文件添加 **Datasource** 、**SqlSessionFactoryBean** 、**MapperScannerConfigurer** 以及 **TransactionManager** 的 Bean。

- Datasource

```properties
jdbc.driverClassName=com.mysql.cj.jdbc.Driver  
jdbc.url=jdbc:mysql://localhost:3306/atguigudb?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai  
jdbc.username=root  
jdbc.password=
```

```java
<context:property-placeholder location="classpath:db.properties" />  
  
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">  
    <property name="url" value="${jdbc.url}" />  
    <property name="driverClassName" value="${jdbc.driverClassName}" />  
    <property name="username" value="${jdbc.username}" />  
    <property name="password" value="${jdbc.password}" />  
</bean>
```

- SqlSessionFactoryBean

```xml
<bean id="sqlSessionFactoryBean" class="org.mybatis.spring.SqlSessionFactoryBean">  
    <property name="dataSource" ref="dataSource"/>  
    <property name="typeAliasesPackage" value="top.xiaorang.entity"/>  
    <property name="mapperLocations">  
        <list>  
            <value>classpath:top/xiaorang/mapper/*Mapper.xml</value>  
        </list>  
    </property>  
</bean>
```

- MapperScannerConfigurer

```xml
<bean id="mapperScannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer">  
    <property name="basePackage" value="top.xiaorang.mapper"/>  
</bean>
```

- TransactionManager

```xml
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">  
    <property name="dataSource" ref="dataSource" />  
</bean>

<tx:annotation-driven transaction-manager="transactionManager"/>
```

#### 4、新建 User 实体类，省略 getter、setter

```java
public class User {  
    private Integer id;  
    private String username;  
    private String password;
}
```

#### 5、创建 user 表

```sql
create table t_user(  
  id integer primary key auto_increment,  
  username varchar(32),  
  password varchar(32) 
);
```

#### 6、新建 UserMapper 接口和 UserMapper.xml

```java
public interface UserMapper {  
    /**  
     * 保存用户信息  
     * @param user 用户信息  
     */  
    void save(User user);  
}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>  
<!DOCTYPE mapper  
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">  
<mapper namespace="top.xiaorang.mapper.UserMapper">  
    <insert id="save" useGeneratedKeys="true">  
        insert into t_user(username, password)  
        values (#{username}, #{password});    </insert>  
</mapper>
```

#### 7、新建 UserService 接口以及实现类 UserServiceImpl

```java
public interface UserService {  
    /**  
     * 注册用户  
     * @param user 用户信息  
     */  
    void register(User user);  
}
```

```java
@Service  
@Transactional(rollbackFor = RuntimeException.class)  
public class UserServiceImpl implements UserService {  
    private final UserMapper userMapper;  
  
    public UserServiceImpl(UserMapper userMapper) {  
        this.userMapper = userMapper;  
    }  
  
    @Override  
    public void register(User user) {  
        userMapper.save(user);  
    }  
}
```

#### 8、新建 UserController 控制器类

```java
@Controller  
@RequestMapping("/user")  
public class UserController {  
    private final UserService userService;  
  
    public UserController(UserService userService) {  
        this.userService = userService;  
    }  
  
    @RequestMapping("/register")  
    public String register(User user) {  
        userService.register(user);  
        return "regOk";  
    }  
}
```

#### 9、创建用户注册页面以及用户注册成功页面

```html
<%@ page contentType="text/html;charset=UTF-8" %>  
<html>  
<head>  
    <title>用户注册</title>  
</head>  
<body>  
<form action="${pageContext.request.contextPath}/user/register" method="post">  
    用户名: <input type="text" name="username"/>  
    密码: <input type="text" name="password"/>  
    <input type="submit" value="submit"/>  
</form>  
</body>  
</html>
```

```html
<%@ page contentType="text/html;charset=UTF-8" %>  
<html>  
<head>  
</head>  
<body>  
<h1>用户注册成功！</h1>  
</body>  
</html>
```

#### 10、测试

发起请求 <http://localhost:8500/ssm/reg.jsp> ，填写用户名和密码，点击提交，浏览器跳转到用户注册成功页面，查看 user 表多了一条记录，表示 ssm 整合成功！🥳

### 2、父子工厂 (容器) 拆分

#### 1、现在 SSM 开发中存在的问题

把 Mvc 层的对象 (Controller、<mvc: annotation-driven />、视图解析器、类型转换器) 与 非 MVC 层的对象 (Datasource、DAO、Service、事务) 都配置在 dispatcher.xml 文件中，最终交给 DispactherServlet 创建的工厂来进行实例化。其中存在着耦合问题，后续开发如果更换 MVC 的实现，如使用 Struts2、WebFlux 等，代码都会受到影响，不利于项目的维护。  
所以该怎么做呢🤔？

- 把单一的工厂进行父子工厂的拆分

子工厂 (DispatcherServlet)，读取 dispatcher.xml 配置文件，完成与 MVC 层相关对象的创建，如视图解析器、自定义类型转换器、静态资源排除、拦截器、视图控制器等。  
父工厂 (ContextLoaderListener)，读取 applicationContext.xml 配置文件，完成与非 MVC 层相关对象的创建，如连接池、DAO、Service、事务、MQ、ES、Redis 等。  
💡需要注意的是，工厂在获取对象时，首先从子工厂中获取对象，如果获取不到，则从父工厂中获取对象。

- 具体步骤

从 dispatcher.xml 配置文件中抽离出非 MVC 层的对象，将这些配置放到 application.xml 配置文件中。

```xml
<context:component-scan base-package="top.xiaorang"/>  
  
<context:property-placeholder location="classpath:db.properties"/>  
  
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">  
    <property name="url" value="${jdbc.url}"/>  
    <property name="driverClassName" value="${jdbc.driverClassName}"/>  
    <property name="username" value="${jdbc.username}"/>  
    <property name="password" value="${jdbc.password}"/>  
</bean>  
  
<bean id="sqlSessionFactoryBean" class="org.mybatis.spring.SqlSessionFactoryBean">  
    <property name="dataSource" ref="dataSource"/>  
    <property name="typeAliasesPackage" value="top.xiaorang.entity"/>  
    <property name="mapperLocations">  
        <list>  
            <value>classpath:top/xiaorang/mapper/*Mapper.xml</value>  
        </list>  
    </property>  
    <!--      需要引入mybatis-config配置文件来配置驼峰命名自动转换-->  
    <property name="configLocation" value="classpath:mybatis-config.xml"/>  
</bean>  
  
<bean id="mapperScannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer">  
    <property name="basePackage" value="top.xiaorang.mapper"/>  
</bean>  
  
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">  
    <property name="dataSource" ref="dataSource"/>  
</bean>  
  
<tx:annotation-driven transaction-manager="transactionManager"/>
```

那么原来的 dispatcher.xml 配置文件中还剩下什么呢？

```xml
<!--    设置注解扫描路径-->  
<context:component-scan base-package="top.xiaorang"/>  
  
<!--    引入SpringMvc的核心功能-->  
<mvc:annotation-driven conversion-service="formattingConversionServiceFactoryBean"/>  
  
<bean id="dateConverter" class="top.xiaorang.converter.DateConverter">  
    <property name="pattern" value="yyyy-MM-dd"/>  
</bean>  
  
<bean id="formattingConversionServiceFactoryBean"  
      class="org.springframework.format.support.FormattingConversionServiceFactoryBean">  
    <property name="converters">  
        <set>  
            <ref bean="dateConverter"/>  
        </set>  
    </property>  
</bean>  
  
<!--    配置视图解析器-->  
<bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">  
    <!--        路径-->  
    <property name="prefix" value="/"/>  
    <!--        文件类型-->  
    <property name="suffix" value=".jsp"/>  
</bean>
```

接下来还要配置 web.xml 文件，增加一个 ContextLoaderListener

```xml
<listener>  
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>  
</listener>  
  
<context-param>  
    <param-name>contextConfigLocation</param-name>  
    <param-value>classpath:applicationContext.xml</param-value>  
</context-param>
```

修改完成之后，测试一下，再次发起请求 <http://localhost:8500/ssm/reg.jsp> ，填写用户名和密码，点击提交，浏览器跳转到用户注册成功页面，user 表中增加一条记录！

#### 2、父子容器存在的问题

但是根据上面这种配置之后，其实还是有点问题，仔细查看控制台打印出来的 DEBUG 信息，发现并 **没有开启事务**！  
![[Pasted image 20220419180704.png]]  
或者查看 Controller 中注入的 Service 是否代理对象也可以辨别到底是否开启事务，断点查看之后，注入进来的 Service 并不是代理对象，所以说明并没有开启事务功能！  
![[Pasted image 20220419171525.png]]  
这是为什么呢🤔？

```ad-important
因为根据我们在父子容器中配置的包扫描路径，在子工厂(DispatcherServlet)，也就是 dispatcher.xml 配置文件中会扫描 Controller 和 Service(没有事务配置，所以不是代理对象)；在父工厂(ContextLoaderListener)，也就是 applicationContext.xml 配置文件也会扫描 Controller 和 Service(存在事务配置，所以是代理对象)。在发起请求时，注入到 Controller 中的 Service 会先从子工厂中获取，如果获取不到，才会从父工厂中获取，但是现在子工厂中存在 Service，所以使用的就是没有被代理的 Service 对象。
```

那么如何解决这个问题呢🤔？  
修改 dispacther.xml 和 applicationContext.xml 配置文件中的包扫描路径。  
针对子容器，即 dispacther.xml 文件，让它只扫描控制器以及与 MVC 相关的内容即可。

```xml
<context:component-scan base-package="top.xiaorang.controller"/>
```

针对父容器，即 applicationContext.xml 文件，让它不扫描控制器与 MVC 相关的内容，即排除掉。

```xml
<context:component-scan base-package="top.xiaorang">  
    <context:exclude-filter type="aspectj" expression="top.xiaorang.controller.*"/>  
</context:component-scan>
```

发起请求 <http://localhost:8500/ssm/reg.jsp> ，填写用户名和密码，点击提交，查看控制台打印信息，发现已经 **成功开启事务功能**！  
![[Pasted image 20220419174149.png]]

# 5、SpringMvc 控制器开发详解 (三)

## 1、核心要点

```ad-important
1. 接收客户端请求参数[讲解完毕]
2. 调用业务对象[讲解完毕]
3. 页面跳转[讲解ing]
```

## 2、JavaWeb 开发中流程跳转的回顾

### 1、JavaWeb 开发中流程跳转的核心代码

```java
public class HelloServlet extends HttpServlet {  
    @Override  
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {  
        // 作用域操作  
        req.setAttribute("request-data", "requestData");  
        HttpSession session = req.getSession();  
        session.setAttribute("session-data", "sessionData");  
        ServletContext application = session.getServletContext();  
        application.setAttribute("application-data", "applicationData");  
  
        // 页面跳转  
        // 转发  
        req.getRequestDispatcher("/result.jsp").forward(req, resp);  
        // 重定向  
        resp.sendRedirect("/result.jsp");  
    }  
}
```

### 2、JavaWeb 页面跳转方式回顾

forward 与 redirect 的核心区别：  
forward ：一次请求；地址栏不变；可以通过 request 作用域传递数据  
redirect：多次请求；地址栏改变；不能通过 request 作用域传递数据；可以跨域跳转  
那么如何选择呢？  
如果本次功能需求，已经完成，则使用 redirect 跳转；如果本次功能需求，没有完成，则使用 forward 跳转。  
关于 redirect 跳转传递数据的错误认知：**redirect 跳转传递数据，因为它是多次请求，所以不能使用 request 作用域传递数据**。解决方式：使用 HttpSession，错！  
为什么不能使用 HttpSession 来解决？因为 HttpSession 只能存储与用户息息相关的数据 (用户特有的数据)，用于会话追踪。非用户相关的数据，不能使用 HttpSession 存储，如果用 Session 存储的话，会大量侵占系统内存资源。  
那么怎么解决呢？**通过 URL 拼接参数的形式完成 redirect 跳转时的数据传递**。

## 3、SpringMvc 的四种跳转方式

四种跳转方式：在 SpringMvc 中控制器与 JSP 或者控制器与控制器之间的跳转。

```ad-info
1. Controller ---> forward ---> JSP
2. Controller ---> redirect ---> JSP
3. Controller ---> forward ---> Controller
4. Controller ---> redirect ---> Controller
```

SpringMvc 的四种跳转方式，在底层其实是对 Servlet 跳转的封装。  
默认情况下，SpringMvc 中控制器通过视图解析器跳转到 JSP 页面时，使用的是 forward 的形式！

### 1、控制器 forward 页面

方式 1：  
除了前面直接使用 return jsp 页面名字，通过与视图解析器 viewResolver 的前缀与后缀进行拼接后得到完整的页面路径，然后进行 forward 跳转。  
方式 2：  
使用 `forward: ` 关键字，视图解析器的拼接将会失效，所以必须 return 完整的 jsp 页面路径，如 return "forward:/result.jsp";

```java
@Controller  
@RequestMapping("/view")  
public class ViewController {  
    @RequestMapping("/forward")  
    public String view1() {  
        System.out.println("view1");  
        return "forward:/result.jsp";  
    }  
}
```

发起请求 <http://localhost:8888/jump/view/view1> ，页面跳转成功！控制台打印：  
![[Pasted image 20220419214920.png]]  
如果使用 "forward: result"; 则会抛出 404 异常！  
![[Pasted image 20220419214631.png]]

### 2、控制器 redirect 页面

使用 ` redirect: ` 关键字，此时视图解析器的拼接也会失效，所以也必须 return 完整的 jsp 页面路径，如 return "redirect:/result.jsp";

```java
@RequestMapping("/redirect")  
public String view2() {  
    System.out.println("view2");  
    return "redirect:/result.jsp";  
}
```

发起请求 <http://localhost:8888/jump/view/redirect> ，页面发生跳转，并且地址栏信息发生改变 <http://localhost:8888/jump/result.jsp> ，控制台打印：  
![[Pasted image 20220419215234.png]]  
如果使用 "redirect: result"；则会抛出 404 异常！  
![[Pasted image 20220419215712.png]]

### 3、控制器 forward 控制器

```java
@Controller  
@RequestMapping("/forward")  
public class ForwardController {  
    @RequestMapping("/forward1")  
    public String forward1() {  
        System.out.println("forward1跳转到forward2中");  
        return "forward:/forward/forward2";  
    }  
  
    @RequestMapping("/forward2")  
    public String forward2() {  
        System.out.println("forward2跳转到jsp页面中");  
        return "forward:/result.jsp";  
    }  
}
```

💡需要注意的是，控制器 forward 控制器时，forward 关键字不能省略！  
发起请求 <http://localhost:8888/jump/forward/forward1> ，页面成功跳转，控制台打印：  
![[Pasted image 20220419220616.png]]

### 4、控制器 redirect 控制器

```java
@Controller  
@RequestMapping("/redirect")  
public class RedirectController {  
    @RequestMapping("/redirect1")  
    public String redirect1() {  
        System.out.println("redirect1跳转到redirect2中");  
        return "redirect:/redirect/redirect2";  
    }  
  
    @RequestMapping("/redirect2")  
    public String redirect2() {  
        System.out.println("redirect2跳转到jsp页面中");  
        return "redirect:/result.jsp";  
    }  
}
```

发起请求 <http://localhost:8888/jump/redirect/redirect1> ，页面成功跳转，地址栏信息发生改变 <http://localhost:8888/jump/result.jsp> ，控制台打印：  
![[Pasted image 20220419221201.png]]

## 4、Web 开发中对于作用域的处理

### 1、回顾 JavaWeb 开发中三种作用域及其使用场景

- HttpServlectRequest，简称 request 作用域，用于组件间跳转时传递数据，其中组件指的是控制器与视图 (JSP)。使用方式：request.setAttribute("name", "value"); request.getAttribute("name");
- HttpSession，简称 session 作用域，不用于组件间跳转时传递数据，而是用于用户会话追踪，只能存储与用户相关的个人数据，如登录状态、令牌、购物车信息等。使用方式：session.setAttribute("name", "value"); session.getAttribute("name");
- ServletContext，简称 application 作用域，全局唯一，框架底层使用，多用于存储全局唯一的对象，如 Spring 中的容器、Hibernate 中的 SessionFactory、Mybatis 中的 SqlSessionFactory 等。使用方式：application.setAttribute("name", "value"); application.getAttribute("name");

### 2、SpringMvc 中对于作用域的处理

#### 1、SpringMvc 中对 request 作用域的处理

- 基于 Model 的方式：

```html
<%@ page contentType="text/html;charset=UTF-8" %>  
<html>  
<head>  
    <title>Title</title>  
</head>  
<body>  
  <h1>This is Request Scope ${requestScope.name}</h1>  
</body>  
</html>
```

```java
@Controller  
@RequestMapping("/view2")  
public class View2Controller {  
    @RequestMapping("/view1")  
    public String view1(Model model) {  
        model.addAttribute("name", "小让");  
        return "result1";  
    }
}
```

发送请求 <http://localhost:8888/jump/view2/view1> ，页面成功跳转，控制台打印：  
![[Pasted image 20220419233755.png]]

- 基于 ModelMap 的方式：

```java
@RequestMapping("/view2")  
public String view2(ModelMap modelMap) {  
    modelMap.addAttribute("name", "小让");  
    return "result1";  
}
```

发送请求 <http://localhost:8888/jump/view2/view2> ，页面成功跳转，控制台打印：  
![[Pasted image 20220419234108.png]]

**Model 与 ModelMap 的细节分析：**

1. 通过 Model 与 ModelMap 进行作用域的处理，就可以解决与视图模板技术耦合的问题。当使用的 jsp 时，Model 和 ModelMap 就会将数据存放到 request 中；当使用的是 FreeMarker 时，Model 和 ModelMap 就会将数据存放到 Root 中。就算切换模板引擎，也不需要修改代码！
2. Model 和 ModelMap 这两种处理 request 作用域的方式，它们的区别是什么🤔？它们表现形式上虽然是通过两种开发方式，声明的形参类型不同，但是在运行的时候，SpringMvc 会动态的提供对应的实现类型，都是 **BindingAwareModelMap**，所以两者 **在本质上是相同的**。
3. 那为什么不直接使用 BindingAwareModelMap 呢🤔？因为在源码中明确表示 SpringMvc 不建议使用 BindingAwareModelMap，如果直接使用 BindingAwareModelMap 的话，会存在耦合。SpringMvc 开发时使用的是 BindingAwareModelMap，而 WebFlux 开发时使用的是 ConcurrentModel。如果替换 Web 的实现方案，直接使用 BindingAwareModelMap 的话，就得修改原有代码，不利于项目的维护，所以更加建议使用 Model 接口处理。
4. SpringMvc 为什么会提供两种开发方式🤔？Model 与 ModelMap 哪种方式更推荐使用🤔？不推荐使用 ModelMap，它是 SpringMvc2.0 引入的类型，当时的设计只是针对 SpringMvc 的场景使用，替换作用域。而后续 Spring 又支持了 WebFlux 的开发方式，显然 ModelMap 就无法使用了，所以 SpringMvc2.5.1 引入了全新设计的 Model 接口，它既可以兼容传统的 SpringMvc 也可以在 WebFlux 中使用，鉴于此 **更推荐使用 Model 的方式**。而 SpringMvc 为了兼容历史遗留问题，所以保留了 ModelMap，导致在 SpringMvc 开发中目前两种方式都可以使用。
5. 如果是 redirect 跳转，数据该如何传递呢🤔？**对于 redirect 跳转，SpringMvc 会自动的把 Model 或者 ModelMap 中的数据通过参数的形式拼接到 URL 上**，从而进行数据的传递。

#### 2、SpringMvc 中对 session 作用域的处理

照样使用 Model 接口来处理，但是需要在控制器类上加上 **@SessionAttributes** 注解，**注解中的 value 值等于你想要放在 session 作用域中的参数的名字**。**没有放置在其中的参数名字**，使用 Model 时就会像上一节知识点一样，**就会将数据存放在 request 作用域中**。如

```java
@Controller  
@RequestMapping("/view3")  
@SessionAttributes(value = {"name", "age"})  
public class View3Controller {  
    @RequestMapping("/view1")  
    public String view1(Model model) {  
        model.addAttribute("name", "小让");  
        model.addAttribute("address", "火星");  
        return "result1";  
    }  
}
```

```html
<%@ page contentType="text/html;charset=UTF-8" %>  
<html>  
<head>  
    <title>Title</title>  
</head>  
<body>  
  <h1>This is Request Scope ${requestScope.name}</h1>  
  <h1>This is Request Scope ${requestScope.address}</h1>  
  <h1>This is Session Scope ${sessionScope.name}</h1>  
</body>  
</html>
```

发起请求 <http://localhost:8888/jump/view3/view1> ，页面跳转成功！控制台打印：  
![[Pasted image 20220420154812.png]]  
其中的 name 会同时存放在 request 作用域和 session 作用域中，而 address 只会存放在 request 作用域中。  
就算 @SessionAttributes 中的 age 没有在 Model 中添加属性，程序也不会报错！  
💡**注意：**  
Model、ModelMap 在把 name 的数据存放在 request 作用域时，检查到类上标注了@SessionAttributes 注解，并且 value 中的值包含 name，则会将 name 所对应的数据对象的引用也存一份放在 session 作用域中。  
**主动删除 Session 作用域中的数据：**

1. 为什么要主动删除 Session 作用域中的数据？因为 HttpSession 对象会长期驻留在内存当中，所以它存储的数据，在不需要时，要及时删除，避免过多的侵占服务器内存资源。而对 Request 作用域而言，就没有这个要求了，因为它的生命周期非常短暂。
2. 需要主动删除 Session 作用域中数据的应用场景：①在 Session 作用域中存储的数据，一旦使用完成，立即删除；②把登录的标记，从 Session 作用域中删除，如注销操作③清空购物车操作
3. 那么怎么实现主动删除 Session 作用域中的数据呢？

```java
@Controller  
@RequestMapping("/view3")  
@SessionAttributes(value = {"name", "age"})  
public class View3Controller {  
    @RequestMapping("/view1")  
    public String view1(Model model) {  
        // 存放在session域和request域中  
        model.addAttribute("name", "小让");  
        // 存放在request域中  
        model.addAttribute("address", "火星");  
        return "forward:/view3/view2";  
    }  
  
    @RequestMapping("/view2")  
    public String view2(SessionStatus sessionStatus) {  
        if(!sessionStatus.isComplete()) {
            // 清除session作用域中的所有数据  
            sessionStatus.setComplete();  
        }  
        return "result2";  
    }  
}
```

修改 view1 方法，让其 forward 到 view2，在 view2 方法中使用 `sessionStatus.setComplete();` 来清除 session 作用域中的所有数据，然后让其 forward 到 result2 页面，此时达到的效果应该是页面中的内容只显示出 request 作用域中的数据，然后 session 作用域中的数据显示不出来！  
发起请求 <http://localhost:8888/jump/view3/view1> ，页面跳转成功！  
![[Pasted image 20220420160018.png]]  
控制台打印：  
![[Pasted image 20220420160152.png]]  
说明 session 作用域中的数据真的被清除了！

#### 3、SpringMvc 中对 application 作用域的处理

**SpringMvc 没有提供替换 application 作用域的功能！**  
为什么 SpringMvc 没有提供对 application 作用域的处理呢🤔？因为 application 作用域 (ServletContext) 这个域是全局唯一的。在开发应用的过程中，多被用于存储全局唯一的对象，被框架底层封装，程序员基本上不会使用其进行业务操作，所以 SpringMvc 没有提供对 application 作用域的处理。  
照这样说，就 **只能使用 Servlet API 来操作 application 作用域**。

### 3、@ModelAttribute 注解

作用：**接收请求参数的同时，把对应的数据存放在 request 作用域当中**。

```java
@Controller  
@RequestMapping("/view4")  
public class View4Controller {  
    @RequestMapping("/view1")  
    public String view1(@ModelAttribute("name") String name) {  
        return "result3";  
    }  
}
```

发起请求 <http://localhost:8888/jump/view4/view1?name=xiaorang> ，页面成功跳转！控制台打印：  
![[Pasted image 20220420163851.png]]  
@ModelAttribute 注解等同于如下效果：

```java
@RequestMapping("/view2")  
public String view2(@RequestParam("name") String name, Model model) {  
    model.addAttribute("name", name);  
    return "result3";  
}
```

**细节分析：**

1. 如果传递的是简单类型的请求参数：@ModelAttribute 注解中的 value 属性必须与请求中请求参数的 key 相同，否则不会接收到值，也不会往 request 作用域中存放数据。
2. 如果传递的是 POJO 类型的请求参数：则没有上述要求，但是@ModelAttribute 注解中 value 属性的值会作为 request 作用域中的 key。

```java
@RequestMapping("/view3")  
public String view3(@ModelAttribute("u") User user) {  
    System.out.println(user);  
    return "result3";  
}
```

```html
<%@ page contentType="text/html;charset=UTF-8" %>  
<html>  
<head>  
    <title>Title</title>  
</head>  
<body>  
  <h1>This is Request Scope ${requestScope.name}</h1>  
  <h1>This is Request Scope ${requestScope.u.username}</h1>  
  <h1>This is Request Scope ${requestScope.u.password}</h1>  
</body>  
</html>
```

发送请求 <http://localhost:8888/jump/view4/view3?username=xiaorang&password=123456> ，页面成功跳转！  
![[Pasted image 20220420165718.png]]  
控制台打印：  
![[Pasted image 20220420165701.png]]  
3. @ModelAttribute 注解中 value 属性的值不能存在于@SessionAttributes 注解的 value 属性中，否则会抛出一个异常！

```java
@Controller  
@RequestMapping("/view4")  
@SessionAttributes(value = "name")  
public class View4Controller {  
    @RequestMapping("/view1")  
    public String view1(@ModelAttribute("name") String name) {  
        return "result3";  
    }
}
```

发起请求 <http://localhost:8888/jump/view4/view1?name=xiaorang> ，页面显示异常！控制台打印出异常信息：  
![[Pasted image 20220420170403.png]]  
如需要将请求参数的数据存放在 session 作用域当中，则只能使用传统的做法 (Model 或者 ModelMap)。

### 4、ModelAndView 技术 (了解)

ModelAndView 这个类型，实际上是一个复合类型，起到两个方面的作用：

1. Model 代表作用域的操作，将是前面讲的 ModelMap，这个类使用的是 ModelMap；
2. View 代表的是跳转路径 (页面)，对应前面讲的 4 种跳转方式

最终这两方面的工作，统一被封装到 ModelAndView 中，作为控制器方法的返回值使用。

```java
@Controller  
@RequestMapping("/view5")  
public class View5Controller {  
    @RequestMapping("/view1")  
    public ModelAndView view1() {  
        ModelAndView modelAndView = new ModelAndView();  
        modelAndView.getModelMap().addAttribute("name", "xiaorang");  
        modelAndView.setViewName("result4");  
        return modelAndView;  
    }  
}
```

```html
<%@ page contentType="text/html;charset=UTF-8" %>  
<html>  
<head>  
    <title>Title</title>  
</head>  
<body>  
  <h1>This is Request Scope ${requestScope.name}</h1>  
</body>  
</html>
```

发起请求 <http://localhost:8888/jump/view5/view1> ，页面成功跳转！控制器打印：  
![[Pasted image 20220420172147.png]]

目前控制器方法返回值：String、ModelAndView。SpringMvc 处理跳转页面与作用域时，把对应的内容最终都会封装到 ModelAndView 中，所以 **ModelAndView 返回值的这种处理更加底层**，而 **返回 String 的处理仅仅是简化了开发**。

## 5、视图控制器

### 1、什么是视图控制器？

**视图控制器** 可以 **通过配置** 的方式，访问 **受保护** 的 **视图模板**，简化开发。

1. 什么是视图模板？JSP、Thymeleaf、FreeMarker、Velocity
2. 为什么需要保护视图模板？目前我们的开发方式，都没有对视图模板进行保护。如果不保护视图模板，用户有可能直接访问视图模板，而不经过 Controller，这样有可能会产生非预期的效果 (Bug)。
3. 如何保护视图模板呢？把 **所有的视图模板都放置在 WEB-INF/jsp 目录下，这样用户就无法通过地址直接访问视图模板** 了。
4. 受保护的视图模板该如何访问呢？此时所有的视图模板，**只能通过控制器 forward 进行访问**。

💡需要注意的是，将视图模板移到 WEB-INF 目录下后，需要修改 dispatcher.xml 配置文件中关于视图解析器的配置，将其前缀改成 "/WEB-INF/jsp/"。此时直接通过地址访问页面则会报 404 异常，找不到文件！只能通过控制器进行访问。

### 2、视图控制器的使用

如果视图模板都受保护了，那么以后访问一个视图模板都得经过一个控制器方法，即需要一个空的控制器方法，专门用来做页面跳转，这样是不是非常麻烦？谁说不是呢！  
这个时候视图控制器的作用就体现出来了！  
在 dispatcher.xml 配置文件增加一个视图控制器即可代替我们专门用来做页面跳转的控制器方法。

```xml
<mvc:view-controller path="/result4" view-name="result4" />
```

此时通过请求 <http://localhost:8888/jump/result4> ，也可以访问到 result4 页面！此时 request 作用域中是不存在数据的。  
![[Pasted image 20220420180659.png]]  
控制台打印：  
![[Pasted image 20220420180638.png]]

### 3、视图控制器的 redirect 跳转

```html
<%@ page contentType="text/html;charset=UTF-8" %>  
<html>  
<head>  
    <title>Title</title>  
</head>  
<body>  
  <h1>This is Result5.jsp</h1>  
</body>  
</html>
```

```java
@Controller  
@RequestMapping("/view6")  
public class View6Controller {  
    @RequestMapping("/view1")  
    public String view1() {  
        return "redirect:/WEB-INF/jsp/result5.jsp";  
    }  
}
```

发起请求 <http://localhost:8888/jump/view6/view1> ，地址栏变成 <http://localhost:8888/jump/WEB-INF/jsp/result5.jsp> ，抛出 404 异常！因为使用的是 redirect 跳转，会让浏览器再发起一次请求，但我们的视图模板已经被保护起来了，所以抛出 404 异常！  
从上一节知识点知道，被保护起来的视图模板需要通过控制器才能访问到，这时我想大家应该知道怎么办了吧！Bingo，**增加一个视图控制器配置，然后 redirect 到该视图控制器配置的 path 即可实现 redirect 跳转**。

1. 增加一个视图控制器

```xml
<mvc:view-controller path="/result5" view-name="result5" />
```

2. 修改上面控制器方法，直接 redirect 到视图控制器配置的 path

```java
@Controller  
@RequestMapping("/view6")  
public class View6Controller {  
    @RequestMapping("/view1")  
    public String view1() {  
        return "redirect:/result5";  
    }  
}
```

再次发起请求  <http://localhost:8888/jump/view6/view1> ，地址栏变成 <http://localhost:8888/jump/result5> ，页面成功跳转！控制台打印：  
![[Pasted image 20220420194006.png]]

## 6、静态资源处理

### 1、无法访问静态资源

所谓的静态资源，指的是在项目中非 java 代码部分的内容，如图片、js 文件、css 文件。  
截至到目前的 SpringMvc 的开发中，按照现有的配置，是无法访问静态资源内容的。

### 2、原因

在现有的 web.xml 配置文件中配置了 DispatcherServlet，拦截所有的请求。

```xml
<servlet>  
    <servlet-name>dispatcherServlet</servlet-name>  
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>  
    <!--        指定SpringMvc配置文件的路径-->  
    <init-param>  
        <param-name>contextConfigLocation</param-name>  
        <param-value>classpath:dispatcher.xml</param-value>  
    </init-param>  
    <!--        本Servlet会在Tomcat启动的时候就会被创建-->  
    <load-on-startup>1</load-on-startup>  
</servlet>  
<servlet-mapping>  
    <servlet-name>dispatcherServlet</servlet-name>  
    <url-pattern>/</url-pattern>  
</servlet-mapping>
```

就算浏览器访问的是静态资源，DispatcherServlet 也并不知道访问的是静态资源，所以还是会去寻找哪个控制器方法的 URL 与之对应，这怎么可能找得到！所以导致静态资源内容访问不了。

### 3、解决方案

#### 1、DefaultServlet

tomcat 的全局配置文件 web.xml 在 $TOMCAT_HOME / conf 目录下，其中配置了一个 DefaultServlet，该 Servlet 是所有 Web 应用程序的默认 Servlet，用于处理静态资源访问的问题。

```xml
<servlet>
    <servlet-name>default</servlet-name>
    <servlet-class>org.apache.catalina.servlets.DefaultServlet</servlet-class>
    <init-param>
        <param-name>debug</param-name>
        <param-value>0</param-value>
    </init-param>
    <init-param>
        <param-name>listings</param-name>
        <param-value>false</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
    </servlet>
```

所以可以在我们应用中的 web.xml 配置文件中增加一段配置用来解决静态资源访问的问题。

```xml
<servlet-mapping>  
    <servlet-name>default</servlet-name>  
    <url-pattern>*.jpg</url-pattern>  
</servlet-mapping>  
<servlet-mapping>  
    <servlet-name>default</servlet-name>  
    <url-pattern>*.js</url-pattern>  
</servlet-mapping>  
<servlet-mapping>  
    <servlet-name>default</servlet-name>  
    <url-pattern>*.css</url-pattern>  
</servlet-mapping>
```

#### 2、default-servlet-handler

第一种解决方案存在的缺点：

1. 配置繁琐
2. 存在与服务器的耦合问题：Tomcat、Jetty、JBoss 或者 GlassFish 等服务器中处理静态资源的 Servlet 名字叫做 `default` ，可是在其他服务器中处理静态资源的 Servlet 叫别的名字，如 Google App Engine 服务器中叫做 `_ah_default`  ，Resin 服务器中的叫做 `  resin-file ` ，WebLogic 服务器中的叫做 `FileServlet` ，WebSphere 服务器中的叫做 `SimpleFileServlet` ，所以当我们切换服务器的时候，需要重新修改 web.xml 配置文件。

所以来看看 SpringMvc 是怎么来解决静态资源访问的问题！**在 SpringMvc 的核心配置文件 dispatcher.xml 中增加一个 <mvc: default-servlet-handler/> 标签即可**。

为什么在 dispatcher.xml 配置文件中增加一个 <mvc: default-servlet-handler/> 标签就可以解决静态资源的访问问题呢🤔？  
其实 **<mvc: default-servlet-handler/>** 这个标签在底层是通过 **DefaultServletHttpRequestHandler**，以 **forward** 的形式调用 **DefaultServlet** 来解决静态资源访问的问题。

```java
public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
    Assert.state(this.servletContext != null, "No ServletContext set");  
    RequestDispatcher rd = this.servletContext.getNamedDispatcher(this.defaultServletName);  
    if (rd == null) {  
        throw new IllegalStateException("A RequestDispatcher could not be located for the default servlet '" + this.defaultServletName + "'");  
    } else {  
        rd.forward(request, response);  
    }  
}
```

发起请求 <http://localhost:8888/jump/img/90179059.jpg> ，断点调试。  
![[Pasted image 20220420210246.png]]

# 6、遇到的问题💣

## 1、Web 版本与 Tomcat 版本不匹配

```ad-bug
org.apache.tomcat.util.descriptor.web.WebXml.setVersion 未知版本字符串 [4.0]。将使用默认版本。
```

![[Pasted image 20220417105952.png]]

```ad-solution
查看 Tomcat 官方文档 [Apache Tomcat® - Which Version Do I Want?](https://tomcat.apache.org/whichversion.html)，可知 Tomcat、Servlet 和 JDK 之间的版本是有要求的，不能随便选择版本。
我们生成的 web. Xml 文档是 4.0 版本的，那么 tomcat 就应该选择 9.0. X。
```

![[Pasted image 20220417111117.png]]

## 2、找到多个名为 spring_web 的片段。这是不合法的相对排序

```ad-solution
将 target 目录删除后，重新编译运行。
```

## 3、Tomcat 启动的时候，控制台输出乱码

```ad-solution
1. 修改IDEA的vmoptions，加上 -Dfile.encoding=UTF-8
2. 配置tomcat启动参数，加上 -Dfile.encoding=UTF-8
```

![[Pasted image 20220417221608.png]]  
![[Pasted image 20220417221839.png]]
