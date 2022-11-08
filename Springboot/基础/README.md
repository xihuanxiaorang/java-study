---
title: SpringBoot基础
tags: springboot 基础
created: 2022-10-29 15:52:59
modified: 2022-10-29 15:52:59
number headings: auto, first-level 1, max 6, _.1.1.
---

`application.yaml` 配置文件提供自定义属性的支持，这样我们就可以将一些常量配置在这里。

```yaml
person:
  last-name: 张三
  age: 18
  boss: true
  birth: 1995/07/13
  maps: { k1: v1, k2: v2 }
  lists:
    - cat
    - dog
    - fish
  dog:
    name: xiaobai
    age: 3
```

然后可以通过 `@value` 注解绑定到你想要的属性上面，如：

```java
@RestController
public class HelloController {
    @Value("${person.last-name}")
    private String name;

    @RequestMapping("hello")
    public String hello() {
        return "hello " + name;
    }
}
```

启动工程，访问 hello，则看到的效果是 hello 张三。

有时候，属性太多，一个个绑定不现实，所以推荐使用第二种方法，**将配置文件中配置的每一个属性的值映射到这个组件中**。

```java
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
	private String lastName;
	private Integer age;
	private boolean boss;
	private Date birth;
	private Map<String, Object> maps;
	private List<Object> lists;
	private Dog dog;
```



`@ConfigurationProperties(prefix = "person")` 告诉 Springboot 将本类中的所有属性与全局配置文件中相关的配置进行绑定，其中 `prefix = “person”` 指明与配置文件中哪个下面的所有属性进行一一映射。



但是需要注意一点，只有这个组件是容器中的组件，才能使用容器提供的 `@ConfigurationProperties` 功能。



在我们在配置文件中给 javaBean 的属性绑定值的时候，可以先导入一个依赖，这样在配置文件中进行绑定的时候就会给出提示。



```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```



`@value` 与@`@ConfigurationProperties` 比较

|              | @ConfigurationProperties | [@value ]() |
| ------------ | ------------------------ | ----------- |
| 功能         | 批量注入配置文件中的属性 | 一个个指定  |
| 松散绑定     | 支持                     | 不支持      |
| SpEL         | 不支持                   | 支持        |
| JSR303 校验  | 支持                     | 不支持      |
| 复杂类型封装 | 支持                     | 不支持      |

- **松散绑定**：类属性名在配置文件中可以使用短横线或下划线来替代驼峰形式命名的属性。
- **SpEL**：EL 表达式

```java
@Value("#{11*2}")
private Integer age;
```

- **JSR303 校验**：数据校验

```java
@Component
@ConfigurationProperties(prefix = "person")
@Validated // 数据校验
public class Person {
	private String lastName;
	@Email // 必须为email格式
	private String email;
```

- 当配置文件中的值不是 email 格式时，则会抛出异常

```plain
Property: person.email
Value: 23234564654
Reason: 不是一个合法的电子邮件地址
```

- **复杂类型封装**：如 **map**，**list** 等，`@value` 并不支持读取复杂类型的配置项。

### 0.1. 