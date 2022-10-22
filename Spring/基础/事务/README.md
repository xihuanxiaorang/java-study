---
title: Spring事务
tags: spring transaction
created: 2022-08-26 01:08:30
modified: 2022-10-18 06:41:37
number headings: auto, first-level 1, max 6, _.1.1.
---

## 1. 环境搭建

> 本章节所涉及到的代码在 [GitHub - xihuanxiaorang/spring-study: 用于spring学习](https://github.com/xihuanxiaorang/spring-study) 仓库中的 `transaction` 模块，可以自行查看。

### 1.1. 创建 spring-study-transaction 模块

选中项目右键新建一个模块，模块名填自己喜欢的即可，这里我就填 `spring-study-transaction`，最后点击确定即可。

![|1261](attachments/Pasted%20image%2020221021090742.png)

### 1.2. 引入相关依赖

在模块的 `pom.xml` 配置文件中引入以下依赖：由于本次 `transaction` 模块需要用到 `jdbc` 模块中已经配置好的组件，所以将 `jdbc` 模块引入进来，还有 `spring-tx` 依赖。

```xml
<dependencies>
    <dependency>
        <groupId>top.xiaorang</groupId>
        <artifactId>spring-study-jdbc</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-tx</artifactId>
    </dependency>
</dependencies>
```

### 1.3. 引入日志配置文件

由于引入了 `logback`，所以需要在资源目录 `resources` 下创建一个 `logback.xml` 配置文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5p %c{1}:%L - %m%n</pattern>
        </encoder>
    </appender>

    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5p %c{1}:%L - %m%n</pattern>
            <charset>utf-8</charset>
        </encoder>
        <file>log/output.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
            <fileNamePattern>log/output.log.%i</fileNamePattern>
        </rollingPolicy>
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>1MB</MaxFileSize>
        </triggeringPolicy>
    </appender>

    <root level="DEBUG">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```

### 1.4. 事务配置类

向 Spring 容器中注入一个 `PlatformTransactionManager` 的实现类 `DataSourceTransactionManager` 组件。

```java
@Configuration
public class TransactionConfig {
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

### 1.5. 主配置类

扫描模块下的所有组件，以及向 Spring 容器中导入 `jdbc` 模块中的主配置类（这样可以让 `jdbc` 模块中的组件注册到 Spring 容器中，否则的话，获取不到 `jdbc` 模块中配置好的组件），最后向 Spring 容器中注入一个 `TransactionTemplate` 组件。

```java
@Configuration
@ComponentScan(basePackages = {"top.xiaorang.spring.transaction"})
@Import(value = {top.xiaorang.spring.jdbc.config.MainConfig.class})
public class MainConfig {
    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
```

这样，环境就搭建好了！可以基于该环境去分别测试编程式事务和声明式事务这两种不同的用法。

## 2. 三大基础设施

Spring 中对事务的支持提供了三大基础设施：

1. `PlatformTransactionManager`
2. `TransactionDefinition`
3. `TransactionStatus`

这三个类是 Spring 处理事务的核心类。

### 2.1. PlatformTransactionManager

`PlatformTransactionManager` 是事务处理的核心，他有诸多的实现类，如下所示：

![|936](attachments/Pasted%20image%2020221021074630.png)

`PlatformTransactionManager` 接口定义如下：

```java
public interface PlatformTransactionManager extends TransactionManager {
    TransactionStatus getTransaction(@Nullable TransactionDefinition definition) throws TransactionException;

    void commit(TransactionStatus status) throws TransactionException;

    void rollback(TransactionStatus status) throws TransactionException;
}
```

可以看到 `PlatformTransactionManager` 中定义了基本的事务操作方法，这些事务操作方法都与平台无关，具体的实现都是由不同的子类来实现的。

这就像 `JDBC` 一样，SUN 公司制定标准，其他数据库厂商提供具体的实现。这么做的好处就是咱们只需要掌握好这套标准即可，不用去管接口的具体实现。以 `PlatformTransactionManager` 为例，它有众多实现，如果你使用的是 `JDBC` 那么可以将 `DataSourceTransactionManager` 作为事务管理器；如果你使用的是 `Hibernate`，那么可以将 `HibernateTransactionManager ` 作为事务管理器；如果你使用的是 `JPA`，那么可以将 `JpaTransactionManager` 作为事务管理器。`DataSourceTransactionManager`、`HibernateTransactionManager ` 以及 `JpaTransactionManager` 都只是 `PlatformTransactionManager` 的具体实现，但是咱们并不需要掌握这些具体实现类的用法，只需要掌握好 `PlatformTransactionManager` 的用法即可。

`PlatformTransactionManager` 中主要有如下三个方法：

1. `getTransaction()` 方法会根据传入的 `TransactionDefinition` 获取一个事务对象，`TransactionDefinition` 中定义了一些事务的基本规则，如传播性、隔离级别等。
2. `commit()` 方法用于提交事务。
3. `rollback()` 方法用于回滚事务。

### 2.2. TransactionDefinition

`TransactionDefinition` 用来描述事务的具体规则，也成为事务的属性。事务有哪些属性呢？主要包括如下 5 个属性：

1. 隔离性
2. 传播性
3. 回滚规则
4. 超时时间
5. 是否只读

`TransactionDefinition` 接口定义如下：

```java
public interface TransactionDefinition {
    int PROPAGATION_REQUIRED = 0;
    int PROPAGATION_SUPPORTS = 1;
    int PROPAGATION_MANDATORY = 2;
    int PROPAGATION_REQUIRES_NEW = 3;
    int PROPAGATION_NOT_SUPPORTED = 4;
    int PROPAGATION_NEVER = 5;
    int PROPAGATION_NESTED = 6;
    int ISOLATION_DEFAULT = -1;
    int ISOLATION_READ_UNCOMMITTED = 1;
    int ISOLATION_READ_COMMITTED = 2;
    int ISOLATION_REPEATABLE_READ = 4;
    int ISOLATION_SERIALIZABLE = 8;
    int TIMEOUT_DEFAULT = -1;

    default int getPropagationBehavior() {
        return 0;
    }

    default int getIsolationLevel() {
        return -1;
    }

    default int getTimeout() {
        return -1;
    }

    default boolean isReadOnly() {
        return false;
    }

    @Nullable
    default String getName() {
        return null;
    }

    static TransactionDefinition withDefaults() {
        return StaticTransactionDefinition.INSTANCE;
    }
}
```

可以看到一共有 5 个方法：

1. `getPropagationBehavior()` 方法，用于获取事务的传播性
2. `getIsolationLevel()` 方法，用于获取事务的隔离级别
3. `getTimeout()` 方法，用于获取事务的超时时间
4. `getName()` 方法，用于获取事务的名称
5. `isReadOnly()` 方法，用于获取事务是否是只读事务

`TransactionDefinition` 也有诸多实现类，如下所示：

![|916](attachments/Pasted%20image%2020221021081205.png)

如果是使用编程式事务进行开发的话，使用 `DefaultTransactionDefinition` 即可。

### 2.3. TransactionStatus

`TransactionStatus` 可以直接理解为事务本身，接口定义如下：

```java
public interface TransactionStatus extends TransactionExecution, SavepointManager, Flushable {
    boolean hasSavepoint();

    void flush();
}
```

该接口继承自 `TransactionExecution`、`SavepointManager` 和 `Flushable` 接口，所以也就具备这些接口所拥有的能力。

```java
public interface TransactionExecution {
    boolean isNewTransaction();

    void setRollbackOnly();

    boolean isRollbackOnly();

    boolean isCompleted();
}

public interface SavepointManager {
    Object createSavepoint() throws TransactionException;

    void rollbackToSavepoint(Object savepoint) throws TransactionException;

    void releaseSavepoint(Object savepoint) throws TransactionException;
}

public interface Flushable {
    /**
     * Flushes this stream by writing any buffered output to the underlying
     * stream.
     *
     * @throws IOException If an I/O error occurs
     */
    void flush() throws IOException;
}
```

1. `hasSavepoint()` 方法用于判断当前是否存在保存点
2. `flush()` 方法用于将底层会话中的修改刷新到数据库，一般用于 `Hibernate/JPA` 的会话，对如 `JDBC` 类型的事务无任何影响
3. `isNewTransaction()` 方法用于判断当前事务是否是一个新事务
4. `setRollbackOnly()` 方法用于设置事务必须回滚
5. `isRollbackOnly()` 方法用于判断事务是否只能回滚
6. `isCompleted()` 方法用于判断事务是否已经结束
7. `createSavepoint()` 方法用于创建一个保存点
8. `rollbackToSavepoint(Object savepoint)` 方法用于回滚到保存点

**以上就是 Spring 中支持事务的三大基础设施**。

## 3. 两种用法

Spring 作为 Java 开发中的基础设施，对于事务也提供了很好的支持，总体上来说，Spring 支持两种类型的事务，**编程式事务** 和 **声明式事务**。其中，编程式事务类似于 JDBC 事务的写法，需要将事务的代码嵌入到业务逻辑中，这样代码的耦合度较高，而声明式事务通过 AOP 的思想能够有效的将事务和业务逻辑代码解耦，因此在实际开发中，声明式事务得到广泛的应用，而编程式事务则较少使用。

### 3.1. 编程式事务

咱们先来看下编程式事务怎么玩。

通过 `PlatformTransactionManager` 或者 `TransactionTemplate` 可以实现编程式事务。如果是在 SpringBoot 项目中，这两个对象 SpringBoot 会自动提供，咱们直接使用即可。但是如果是在传统的 SSM 项目中，则需要咱们通过配置来提供这两个对象，在环境搭建环节已经成功这两个对象注入到 Spring 容器中，现在直接拿出来用即可。

```java
@Service
public class TransService {
    private final JdbcTemplate jdbcTemplate;
    private final PlatformTransactionManager transactionManager;

    public TransService(JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionManager = transactionManager;
    }

    public void transfer() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            jdbcTemplate.update("update account set balance = balance + 100 where name = 'javaboy'");
            int i = 1 / 0;
            jdbcTemplate.update("update account set balance = balance - 100 where name = 'itboyhub'");
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(transactionStatus);
        }
    }
}
```

这段代码很简单，没啥好解释的，在 `try...catch...` 中进行业务操作，没问题就 `commit`，有问题就 `rollback`。如果我们需要配置事务的隔离性、传播性等，可以在 `DefaultTransactionDefinition` 对象中进行配置。

测试结果如下所示：

![](attachments/Pasted%20image%2020221021211531.png)

上面的代码是通过 `PlatformTransactionManager` 实现的编程式事务，我们也可以通过 `TransactionTemplate` 来实现编程式事务，如下：

```java
@Service  
public class TransService {  
    private final JdbcTemplate jdbcTemplate;  
    private final TransactionTemplate transactionTemplate;  
  
    public TransService(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {  
        this.jdbcTemplate = jdbcTemplate;  
        this.transactionTemplate = transactionTemplate;  
    }  
  
  
    public void transfer() {  
        transactionTemplate.executeWithoutResult((transactionStatus) -> {  
            jdbcTemplate.update("update account set balance = balance + 100 where name = 'javaboy'");  
            int i = 1 / 0;  
            jdbcTemplate.update("update account set balance = balance - 100 where name = 'itboyhub'");  
        });  
    }  
}
```

直接注入 `TransactionTemplate`，然后在 `action()` 回调方法方法中书写核心业务即可。上面这种不需要获取事务执行的结果，直接使用 `executeWithoutResult()` 方法即可；如果想要获取事务的执行结果，则使用 `execute()` 方法。

测试结果如下所示：其实与通过 `PlatformTransactionManager` 实现编程式事务这种方式没有什么区别，只不过 `TransactionTemplate` 对 `PlatformTransactionManager` 操作进一步封装了而已。

![](attachments/Pasted%20image%2020221021213311.png)

这就是两种编程式事务的玩法。由于编程式事务代码侵入性太严重了，因此在实际开发中很少使用，在咱们的项目中使用的更多的是声明式事务。

### 3.2. 声明式事务

声明式事务如果使用 `XML` 配置，可以做到无侵入；如果使用 `Java` 配置，也只有一个 `@Transactional` 注解侵入而已，相对来说非常容易。

以下配置针对传统 SSM 项目（因为在 SpringBoot 项目中，事务相关的组件已经配置好了）：

#### 3.2.1. XML 配置

`XML` 配置声明式事务大致上可以分为四个步骤，如下：

1. 配置数据源

   如果使用 `XML` 并且不配置包扫描的话，则需要将 `jdbc` 模块中配置好的数据源 `DataSource` 和 `JdbcTemplate` 组件在配置文件中再配置一份。

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xmlns="http://www.springframework.org/schema/beans"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
       <context:property-placeholder location="classpath:db.properties"/>
   
       <bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource">
           <property name="driverClassName" value="${jdbc.driverClassName}"/>
           <property name="jdbcUrl" value="${jdbc.url}"/>
           <property name="username" value="${jdbc.username}"/>
           <property name="password" value="${jdbc.password}"/>
           <property name="connectionTimeout" value="${jdbc.connectionTimeout}"/>
           <property name="readOnly" value="${jdbc.readOnly}"/>
           <property name="idleTimeout" value="${jdbc.pool.idleTimeout}"/>
           <property name="maxLifetime" value="${jdbc.pool.maxLifetime}"/>
           <property name="maximumPoolSize" value="${jdbc.pool.maxPoolSize}"/>
           <property name="minimumIdle" value="${jdbc.pool.minIdle}"/>
       </bean>
   
       <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
           <property name="dataSource" ref="dataSource"/>
       </bean>
   </beans>
   ```

2. 配置事务管理器

   其中的 `TransactionTemplate` 可配可不配，只是为了事务操作时方便

   ```xml
   <bean class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
       <property name="dataSource" ref="dataSource"/>
   </bean>
   
   <bean id="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate">
       <property name="transactionManager" ref="dataSourceTransactionManager"/>
   </bean>
   ```

3. 配置事务通知

   ```xml
   <tx:advice id="txAdvice" transaction-manager="dataSourceTransactionManager">
       <tx:attributes>
           <tx:method name="m3"/>
           <tx:method name="m4"/>
       </tx:attributes>
   </tx:advice>
   ```

4. 配置 AOP

   ```xml
   <aop:config>
       <aop:pointcut id="pc" expression="execution(* top.xiaorang.spring.transaction.service.*.*(..))"/>
       <aop:advisor advice-ref="txAdvice" pointcut-ref="pc"/>
   </aop:config>
   ```

第三步与第四步中定义出来的方法交集，就是咱们要添加事务的方法。

配置完成之后，如下一些方法就自动具备事务了：

```java
public class UserService {
    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void m3() {
        jdbcTemplate.update("update account set balance = balance + 100 where name = 'javaboy'");
        int i = 1 / 0;
        jdbcTemplate.update("update account set balance = balance - 100 where name = 'itboyhub'");
    }
}
```

咱们来测试一下效果，不过在测试之前，需要再在 `XML` 配置文件中将 `UserService` 组件注入到 Spring 容器中。

```xml
<bean id="userService" class="top.xiaorang.spring.transaction.service.UserService">
    <constructor-arg name="jdbcTemplate" ref="jdbcTemplate"/>
</bean>
```

测试类：

```java
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void m3() {
        userService.m3();
    }
}
```

点击测试按钮，报错信息居然不是 `/ by Zero`，而是报找不到 `AspectJExpressionPointcut` 类。

![](attachments/Pasted%20image%2020221022025900.png)

所以咱们猜想应该还需要引入 `AOP` 相关依赖：

```xml
<dependency>  
    <groupId>org.springframework</groupId>  
    <artifactId>spring-aop</artifactId>  
</dependency>  
<dependency>  
    <groupId>org.springframework</groupId>  
    <artifactId>spring-aspects</artifactId>  
</dependency>
```

由于在 `aop` 模块咱们已经引入了 `AOP` 相关依赖，所以咱们可以直接引入 `aop` 模块即可。

```xml
<dependency>
    <groupId>top.xiaorang</groupId>
    <artifactId>spring-study-aop</artifactId>
</dependency>
```

再次点击测试按钮，测试结果如下所示：虽然报错了，但是符合咱们的预期，从日志信息可以发现与编程式事务没有什么区别。

![image-20221022030808751](attachments/image-20221022030808751.png)

#### 3.2.2. Java 配置

咱们也可以使用 `Java` 配置这种方式来实现声明式事务，这种方式就不再需要 `XML` 配置文件了，只需要在咱们的主配置类 `MainConfig` 加上 `@EnableTransactionManagement` 注解即可开启事务支持。

```java
@Configuration
@ComponentScan(basePackages = {"top.xiaorang.spring.transaction"})
@Import(value = {top.xiaorang.spring.jdbc.config.MainConfig.class})
@EnableTransactionManagement //开启事务注解支持
public class MainConfig {
    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
```

之后，哪个方法需要事务就在哪个方法上添加 `@Transactional` 注解即可，如下所示：

```java
@Service
public class UserService {
    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void m3() {
        jdbcTemplate.update("update account set balance = balance + 100 where name = 'javaboy'");
        int i = 1 / 0;
        jdbcTemplate.update("update account set balance = balance - 100 where name = 'itboyhub'");
    }
}
```

测试类改成使用 `MainConfig`，不使用 `XML` 配置文件。

```java
@ContextConfiguration(classes = {MainConfig.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void m3() {
        userService.m3();
    }
}
```

点击测试按钮，可以发现测试结果与使用 `XML` 配置时一模一样。

![image-20221022030808751](attachments/image-20221022030808751.png)

当然这个稍微有点代码入侵，不过问题不大，日常开发中这种方式使用较多。当 `@Transactional` 注解加在类上时，表示该类中的所有方法都有事务，该注解加在方法上面，表示只有该方法才有事务。

## 4. 事务属性

在前面的配置中，咱们只是提到事务的用法，并没有说到事务的一些属性细节，现在咱们就来自此捋一捋事务中的五大属性。

### 4.1. 隔离性

首先就是事务的隔离性，也就是事务的隔离级别。

MySQL 中有四种不同的隔离级别，这四种不同的隔离级别在 Spring 中都得到了很好的支持。Spring 中默认的事务隔离级就是 `default`，即数据库本身的隔离级别是啥就是啥，`default` 就能满足咱们日常开发中的大部分场景，没有必要不需要修改。不过如果项目有需要，咱们也可以随时调整事务的隔离级别，调整方式如下：

#### 4.1.1. 编程式事务隔离级别

如果是编程式事务，可以通过如下方式修改事务的隔离级别：

##### 4.1.1.1. TransactionTemplate

在往 Spring 容器中注入 `TransactionTemplate` 组件的时候，由于 `TransactionTemplate` 继承自 `DefaultTransactionDefinition`，所以直接修改对象中的 `isolationLevel` 属性即可。

```java
@Bean
public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
    TransactionTemplate transactionTemplate = new TransactionTemplate();
    transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
    return transactionTemplate;
}
```

##### 4.1.1.2. PlatformTransactionManager

调用 `PlatformTransactionManager` 组件中的 `getTransaction()` 方法获取一个事务对象时，需要传入一个 `TransactionDefinition` 接口的实现类对象，咱们传入的是 `TransactionDefinition` 的默认实现类 `DefaultTransactionDefinition` 对象，所以咱们也只需要直接修改对象中的 `isolationLevel` 属性即可。

```java
DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
```

#### 4.1.2. 声明式事务隔离级别

如果是声明式事务可以通过如下方式修改隔离级别：

##### 4.1.2.1. XML 配置

修改 `m3()` 方法的事务隔离级别为 `SERIALIZABLE`。

```xml
<tx:advice id="txAdvice" transaction-manager="dataSourceTransactionManager">
    <tx:attributes>
        <tx:method name="m3" isolation="SERIALIZABLE"/>
        <tx:method name="m4"/>
    </tx:attributes>
</tx:advice>
```

##### 4.1.2.2. @Transactional 注解配置

修改 `@Transactional` 注解中的 `isolation` 属性为 `SERIALIZABLE`。

```java
@Transactional(isolation = Isolation.SERIALIZABLE)
public void m3() {
    jdbcTemplate.update("update account set balance = balance + 100 where name = 'javaboy'");
    int i = 1 / 0;
    jdbcTemplate.update("update account set balance = balance - 100 where name = 'itboyhub'");
}
```

### 4.2. 传播性

> 事务传播行为是为了解决业务层方法之间互相调用的事务问题，当一个事务方法被另一个事务方法调用时，事务该以何种状态存在？例如新方法可能继续在现有事务中运行，也可能开启一个新事务，并在自己的事务中运行，等等，这些规则就涉及到事务的传播性。

关于事务的传播性，Spring 主要定义了如下七种：

```java
public enum Propagation {
    REQUIRED(0),
    SUPPORTS(1),
    MANDATORY(2),
    REQUIRES_NEW(3),
    NOT_SUPPORTED(4),
    NEVER(5),
    NESTED(6);

    private final int value;

    private Propagation(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
```

具体含义如下：

| 传播性          | 描述                                                         |
| --------------- | ------------------------------------------------------------ |
| `REQUIRED`      | 如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务 |
| `SUPPORTS`      | 如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行 |
| `MANDATORY`     | 如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常 |
| `REQUIRES_NEW`  | 创建一个新的事务，如果当前存在事务，则把当前事务挂起         |
| `NOT_SUPPORTED` | 以非事务运行，如果当前存在事务，则把当前事务挂起             |
| `NEVER`         | 以非事务运行，如果当前存在事务，则抛出异常                   |
| `NESTED`        | 如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于 `REQUIRED` |

#### 4.2.1. 使用方式

配置传播性其实与配置隔离级别时的情况差不多。

##### 4.2.1.1. 在 TransactionTemplate 中的配置

```java
@Bean
public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
    TransactionTemplate transactionTemplate = new TransactionTemplate();
    transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    return transactionTemplate;
}
```

##### 4.2.1.2. 在 PlatformTransactionManager 中的配置

```java
DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
```

##### 4.2.1.3. 在 XML 中的配置

```xml
<tx:advice id="txAdvice" transaction-manager="dataSourceTransactionManager">
    <tx:attributes>
        <tx:method name="m3" propagation="REQUIRED"/>
        <tx:method name="m4"/>
    </tx:attributes>
</tx:advice>
```

##### 4.2.1.4. 在 @Transactional 注解中的配置

```java
@Transactional(propagation = Propagation.REQUIRED)
public void m3() {
    jdbcTemplate.update("update account set balance = balance + 100 where name = 'javaboy'");
    int i = 1 / 0;
    jdbcTemplate.update("update account set balance = balance - 100 where name = 'itboyhub'");
}
```

#### 4.2.2. 七种传播性演示

##### 4.2.2.1. REQUIRED

`REQUIRED` 表示如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。

例如有如下一段代码：

```java
@Service
public class AccountService {
    private final JdbcTemplate jdbcTemplate;

    public AccountService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void handle1() {
        jdbcTemplate.update("update account set balance = ? where id = ?", 1, 2);
    }
}
```

```java
@Service
public class AccountService2 {
    private final JdbcTemplate jdbcTemplate;
    private final AccountService accountService;

    public AccountService2(JdbcTemplate jdbcTemplate, AccountService accountService) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountService = accountService;
    }

    @Transactional
    public void handle2() {
        jdbcTemplate.update("update account set balance = ? where name = ?", 1, "javaboy");
        accountService.handle1();
    }
}
```

在 `handle2()` 方法中调用 `handle1()` 方法。那么：

1. 如果 `handle2()` 方法本身是有事务的，则 `handle1()` 方法就会加入到 `handle2()` 方法所在的事务中，这样两个方法将处于同一个事务中，一起成功或者一起失败（不管 `handle2()` 方法还是 `handle1()` 方法谁抛异常，都会导致整体回滚）。
2. 如果 `handle2()` 方法本身是没有事务的，则 `handle1()` 方法就会自己开启一个新的事务，自己玩。

测试一下如下情况：`handle2()` 方法有事务，`handle1()` 方法也有事务。测试类如下：

```java
@ContextConfiguration(classes = {MainConfig.class})
@ExtendWith(SpringExtension.class)
class AccountService2Test {
    @Autowired
    private AccountService2 accountService2;

    @Test
    void handle2() {
        accountService2.handle2();
    }
}
```

测试结果如下所示：

```
2022-10-22 17:56:30.553 [main] DEBUG o.s.j.d.DataSourceTransactionManager:370 - Creating new transaction with name [top.xiaorang.spring.transaction.service.AccountService2.handle2]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
2022-10-22 17:56:30.701 [main] DEBUG o.s.j.d.DataSourceTransactionManager:267 - Acquired Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] for JDBC transaction
2022-10-22 17:56:30.703 [main] DEBUG o.s.j.d.DataSourceTransactionManager:285 - Switching JDBC Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] to manual commit
2022-10-22 17:56:30.708 [main] DEBUG o.s.j.c.JdbcTemplate:958 - Executing prepared SQL update
2022-10-22 17:56:30.708 [main] DEBUG o.s.j.c.JdbcTemplate:643 - Executing prepared SQL statement [update account set balance = ? where name = ?]
2022-10-22 17:56:30.725 [main] DEBUG o.s.j.d.DataSourceTransactionManager:470 - Participating in existing transaction
2022-10-22 17:56:30.728 [main] DEBUG o.s.j.c.JdbcTemplate:958 - Executing prepared SQL update
2022-10-22 17:56:30.728 [main] DEBUG o.s.j.c.JdbcTemplate:643 - Executing prepared SQL statement [update account set balance = ? where id = ?]
2022-10-22 17:56:30.729 [main] DEBUG o.s.j.d.DataSourceTransactionManager:740 - Initiating transaction commit
2022-10-22 17:56:30.729 [main] DEBUG o.s.j.d.DataSourceTransactionManager:330 - Committing JDBC transaction on Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58]
2022-10-22 17:56:30.732 [main] DEBUG o.s.j.d.DataSourceTransactionManager:389 - Releasing JDBC Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] after transaction
```

从打印出来的日志信息可以看出，在执行到 `handle2()` 方法的时候会开启一个新的事务（`Creating new transaction`），在执行到 `handle1()` 方法的时候会加入到 `handle2()` 方法所在的事务（`Participating in existing transaction`），并不会再开启一个新的事务。

##### 4.2.2.2. REQUIRES_NEW

`REQUIRES_NEW` 表示创建一个新的事务，如果当前存在事务，则把 **当前事务挂起**。换言之，不管外部方法是否有事务，`REQUIRES_NEW` 都会开启自己的事务。

有的小伙伴可能觉得 `REQUIRES_NEW` 与 `REQUIRED` 很像，似乎没啥区别。其实要是单纯看最终的回滚效果，可能确实看不到啥区别。但是，在 `REQUIRES_NEW` 中可能会同时存在两个事务，外部方法的事务被挂起，内部方法的事务独立运行，而在 `REQUIRED` 中则不会出现这种情况，如果内外部方法传播性都是 `REQUIRED`，那么最终也只是一个事务。

还是上面那个例子，假设 `handle1()` 方法和 `handle2()` 方法都有事务，`handle2()` 方法的事务传播性是 `REQUIRED`，而 `handle1()` 方法的事务传播性是 `REQUIRES_NEW`，那么最终打印出来的事务日志如下所示：

```
2022-10-22 18:15:29.139 [main] DEBUG o.s.j.d.DataSourceTransactionManager:370 - Creating new transaction with name [top.xiaorang.spring.transaction.service.AccountService2.handle2]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
2022-10-22 18:15:29.265 [main] DEBUG o.s.j.d.DataSourceTransactionManager:267 - Acquired Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] for JDBC transaction
2022-10-22 18:15:29.268 [main] DEBUG o.s.j.d.DataSourceTransactionManager:285 - Switching JDBC Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] to manual commit
2022-10-22 18:15:29.273 [main] DEBUG o.s.j.c.JdbcTemplate:958 - Executing prepared SQL update
2022-10-22 18:15:29.273 [main] DEBUG o.s.j.c.JdbcTemplate:643 - Executing prepared SQL statement [update account set balance = ? where name = ?]
2022-10-22 18:15:29.290 [main] DEBUG o.s.j.d.DataSourceTransactionManager:429 - Suspending current transaction, creating new transaction with name [top.xiaorang.spring.transaction.service.AccountService.handle1]
2022-10-22 18:15:29.294 [HikariPool-1 connection adder] DEBUG c.z.h.p.HikariPool:738 - HikariPool-1 - Added connection com.mysql.cj.jdbc.ConnectionImpl@26f5a28e
2022-10-22 18:15:29.294 [main] DEBUG o.s.j.d.DataSourceTransactionManager:267 - Acquired Connection [HikariProxyConnection@2104842259 wrapping com.mysql.cj.jdbc.ConnectionImpl@26f5a28e] for JDBC transaction
2022-10-22 18:15:29.295 [main] DEBUG o.s.j.d.DataSourceTransactionManager:285 - Switching JDBC Connection [HikariProxyConnection@2104842259 wrapping com.mysql.cj.jdbc.ConnectionImpl@26f5a28e] to manual commit
2022-10-22 18:15:29.298 [main] DEBUG o.s.j.c.JdbcTemplate:958 - Executing prepared SQL update
2022-10-22 18:15:29.299 [main] DEBUG o.s.j.c.JdbcTemplate:643 - Executing prepared SQL statement [update account set balance = ? where id = ?]
2022-10-22 18:15:29.300 [main] DEBUG o.s.j.d.DataSourceTransactionManager:740 - Initiating transaction commit
2022-10-22 18:15:29.300 [main] DEBUG o.s.j.d.DataSourceTransactionManager:330 - Committing JDBC transaction on Connection [HikariProxyConnection@2104842259 wrapping com.mysql.cj.jdbc.ConnectionImpl@26f5a28e]
2022-10-22 18:15:29.300 [main] DEBUG o.s.j.d.DataSourceTransactionManager:389 - Releasing JDBC Connection [HikariProxyConnection@2104842259 wrapping com.mysql.cj.jdbc.ConnectionImpl@26f5a28e] after transaction
2022-10-22 18:15:29.304 [main] DEBUG o.s.j.d.DataSourceTransactionManager:996 - Resuming suspended transaction after completion of inner transaction
2022-10-22 18:15:29.305 [main] DEBUG o.s.j.d.DataSourceTransactionManager:740 - Initiating transaction commit
2022-10-22 18:15:29.305 [main] DEBUG o.s.j.d.DataSourceTransactionManager:330 - Committing JDBC transaction on Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58]
2022-10-22 18:15:29.305 [main] DEBUG o.s.j.d.DataSourceTransactionManager:389 - Releasing JDBC Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] after transaction
```

分析这段日志可以看出：

1. 首先为 `handle2()` 方法开启一个新事务 `Creating new transaction`
2. 执行完 `handle2()` 方法中的 SQL 之后，先将当前存在的事务挂起，然后为 `handle1()` 方法开启一个新的事务 `Suspending current transaction, creating new transaction`
3. 执行完 `handle1()` 方法中的 SQL，提交 `handle1()` 方法的事务
4. 恢复被挂起的事务 `Resuming suspended transaction after completion of inner transaction`
5. 提交 `handle2()` 方法的事务

从这段日志中大家可以非常明确的看到 `REQUIRES_NEW` 与 `REQUIRED` 的区别。

再来简单总结一下（假设 `handle1()` 方法的传播性是 `REQUIRES_NEW`）：

1. 如果 `handle2()` 方法没有事务，`handle1()` 方法会自己开启一个事务自己玩
2. 如果 `handle2()` 方法有事务，`handle1()` 方法还是会开启一个事务。此时，如果 `handle2()` 方法发生了异常进行回滚，并不会导致 `handle1()` 方法回滚，因为 `handle1()` 方法是独立的事务；如果 `handle1()` 方法发生了异常导致回滚，并且 `handle1()` 方法的异常没有被捕获处理传到了 `handle2()` 方法中，那么也会导致 `handle2()` 方法回滚。

> 这个地方小伙伴们要稍微注意一下，在测试的时候，由于是两个更新语句，如果更新的查询字段不是索引字段，那么 InnoDB 将使用表锁，这样就会发生死锁（`handle1()` 方法执行时开启表锁，导致 `handle1()` 陷入等待中，而必须等 `handle1()` 方法执行完，`handle2()` 方法才能释放锁）。所以，在上面的测试中，需要将 `name` 字段设置为索引字段，这样默认就使用行锁了。

##### 4.2.2.3. NESTED

`NESTED`**(嵌套)**，表示如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于 `REQUIRED`，即创建一个新的事务。

假设 `handle2()` 方法有事务,`handle1()` 方法也有事务并且传播性为 `NESTED`，那么最终打印出来的事务日志如下所示：

```
2022-10-22 19:12:11.495 [main] DEBUG o.s.j.d.DataSourceTransactionManager:370 - Creating new transaction with name [top.xiaorang.spring.transaction.service.AccountService2.handle2]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
2022-10-22 19:12:11.633 [main] DEBUG o.s.j.d.DataSourceTransactionManager:267 - Acquired Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] for JDBC transaction
2022-10-22 19:12:11.636 [main] DEBUG o.s.j.d.DataSourceTransactionManager:285 - Switching JDBC Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] to manual commit
2022-10-22 19:12:11.641 [main] DEBUG o.s.j.c.JdbcTemplate:958 - Executing prepared SQL update
2022-10-22 19:12:11.642 [main] DEBUG o.s.j.c.JdbcTemplate:643 - Executing prepared SQL statement [update account set balance = ? where name = ?]
2022-10-22 19:12:11.655 [main] DEBUG o.s.j.d.DataSourceTransactionManager:449 - Creating nested transaction with name [top.xiaorang.spring.transaction.service.AccountService.handle1]
2022-10-22 19:12:11.662 [main] DEBUG o.s.j.c.JdbcTemplate:958 - Executing prepared SQL update
2022-10-22 19:12:11.663 [main] DEBUG o.s.j.c.JdbcTemplate:643 - Executing prepared SQL statement [update account set balance = ? where id = ?]
2022-10-22 19:12:11.663 [main] DEBUG o.s.j.d.DataSourceTransactionManager:733 - Releasing transaction savepoint
2022-10-22 19:12:11.663 [main] DEBUG o.s.j.d.DataSourceTransactionManager:740 - Initiating transaction commit
2022-10-22 19:12:11.664 [main] DEBUG o.s.j.d.DataSourceTransactionManager:330 - Committing JDBC transaction on Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58]
2022-10-22 19:12:11.664 [main] DEBUG o.s.j.d.DataSourceTransactionManager:389 - Releasing JDBC Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] after transaction
```

关键一句在 `Creating nested transaction`。此时，`NESTED` 修饰的内部方法 `handle1()` 方法属于外部事务的子事务，外部主事务回滚的话，子事务也会回滚，而内部子事务可以单独回滚而不影响外部主事务和其他子事务（前提是需要处理掉内部子事务的异常）。

##### 4.2.2.4. MANDATORY

`MANDATORY`**(强制性的)**，表示如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。

这个好理解，举两个例子：

假设 `handle2()` 方法有事务,`handle1()` 方法也有事务并且传播性为 `MANDATORY`，那么最终打印出来的事务日志如下所示：

```
2022-10-22 19:21:21.616 [main] DEBUG o.s.j.d.DataSourceTransactionManager:370 - Creating new transaction with name [top.xiaorang.spring.transaction.service.AccountService2.handle2]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
2022-10-22 19:21:21.739 [main] DEBUG o.s.j.d.DataSourceTransactionManager:267 - Acquired Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] for JDBC transaction
2022-10-22 19:21:21.739 [main] DEBUG o.s.j.d.DataSourceTransactionManager:285 - Switching JDBC Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] to manual commit
2022-10-22 19:21:21.745 [main] DEBUG o.s.j.c.JdbcTemplate:958 - Executing prepared SQL update
2022-10-22 19:21:21.745 [main] DEBUG o.s.j.c.JdbcTemplate:643 - Executing prepared SQL statement [update account set balance = ? where name = ?]
2022-10-22 19:21:21.755 [main] DEBUG o.s.j.d.DataSourceTransactionManager:470 - Participating in existing transaction
2022-10-22 19:21:21.755 [main] DEBUG o.s.j.c.JdbcTemplate:958 - Executing prepared SQL update
2022-10-22 19:21:21.755 [main] DEBUG o.s.j.c.JdbcTemplate:643 - Executing prepared SQL statement [update account set balance = ? where id = ?]
2022-10-22 19:21:21.765 [main] DEBUG o.s.j.d.DataSourceTransactionManager:740 - Initiating transaction commit
2022-10-22 19:21:21.765 [main] DEBUG o.s.j.d.DataSourceTransactionManager:330 - Committing JDBC transaction on Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58]
2022-10-22 19:21:21.765 [main] DEBUG o.s.j.d.DataSourceTransactionManager:389 - Releasing JDBC Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] after transaction
```

从打印出来的日志信息可以看出，在执行到 `handle2()` 方法的时候会开启一个新的事务（`Creating new transaction`），在执行到 `handle1()` 方法的时候会加入到 `handle2()` 方法所在的事务（`Participating in existing transaction`），并不会再开启一个新的事务。效果等价于 `REQUIRED`。

假设 `handle2()` 方法无事务,`handle1()` 方法有事务并且传播性为 `MANDATORY`，那么最终执行时会抛出如下异常：

```
org.springframework.transaction.IllegalTransactionStateException: No existing transaction found for transaction marked with propagation 'mandatory'
```

由于没有已经存在的事务，所以会抛出异常。

##### 4.2.2.5. SUPPORTS

`SUPPORTS` 表示如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。

这个也很简单，举两个例子大家就明白了。

假设 `handle2()` 方法有事务,`handle1()` 方法也有事务并且传播性为 `SUPPORTS`，那么最终打印出来的事务日志如下所示：

```
2022-10-22 19:28:43.871 [main] DEBUG o.s.j.d.DataSourceTransactionManager:370 - Creating new transaction with name [top.xiaorang.spring.transaction.service.AccountService2.handle2]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
2022-10-22 19:28:44.003 [main] DEBUG o.s.j.d.DataSourceTransactionManager:267 - Acquired Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] for JDBC transaction
2022-10-22 19:28:44.005 [main] DEBUG o.s.j.d.DataSourceTransactionManager:285 - Switching JDBC Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] to manual commit
2022-10-22 19:28:44.009 [main] DEBUG o.s.j.c.JdbcTemplate:958 - Executing prepared SQL update
2022-10-22 19:28:44.010 [main] DEBUG o.s.j.c.JdbcTemplate:643 - Executing prepared SQL statement [update account set balance = ? where name = ?]
2022-10-22 19:28:44.022 [main] DEBUG o.s.j.d.DataSourceTransactionManager:470 - Participating in existing transaction
2022-10-22 19:28:44.026 [main] DEBUG o.s.j.c.JdbcTemplate:958 - Executing prepared SQL update
2022-10-22 19:28:44.026 [main] DEBUG o.s.j.c.JdbcTemplate:643 - Executing prepared SQL statement [update account set balance = ? where id = ?]
2022-10-22 19:28:44.027 [main] DEBUG o.s.j.d.DataSourceTransactionManager:740 - Initiating transaction commit
2022-10-22 19:28:44.027 [main] DEBUG o.s.j.d.DataSourceTransactionManager:330 - Committing JDBC transaction on Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58]
2022-10-22 19:28:44.028 [main] DEBUG o.s.j.d.DataSourceTransactionManager:389 - Releasing JDBC Connection [HikariProxyConnection@1175154004 wrapping com.mysql.cj.jdbc.ConnectionImpl@2787de58] after transaction
```

这段日志很简单，没啥好说的，认证 `Participating in existing transaction` 表示加入到已经存在的事务中即可。效果等价于 `REQUIRED`。

假设 `handle2()` 方法无事务,`handle1()` 方法有事务并且传播性为 `SUPPORTS`，这个最终就不会开启事务，也就没有事务相关日志。

##### 4.2.2.6. NOT_SUPPORTED

`NOT_SUPPORTED` 表示以非事务方式运行，如果当前存在事务，则把当前事务挂起。

假设 `handle2()` 方法有事务,`handle1()` 方法也有事务并且传播性为 `NOT_SUPPORTED`，那么最终打印出来的事务日志如下所示：

```
2022-10-23 01:18:24.633 [main] DEBUG o.s.j.d.DataSourceTransactionManager:370 - Creating new transaction with name [top.xiaorang.spring.transaction.service.AccountService2.handle2]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
2022-10-23 01:18:24.844 [main] DEBUG o.s.j.d.DataSourceTransactionManager:267 - Acquired Connection [HikariProxyConnection@666312528 wrapping com.mysql.cj.jdbc.ConnectionImpl@5b022357] for JDBC transaction
2022-10-23 01:18:24.847 [main] DEBUG o.s.j.d.DataSourceTransactionManager:285 - Switching JDBC Connection [HikariProxyConnection@666312528 wrapping com.mysql.cj.jdbc.ConnectionImpl@5b022357] to manual commit
2022-10-23 01:18:24.853 [main] DEBUG o.s.j.c.JdbcTemplate:958 - Executing prepared SQL update
2022-10-23 01:18:24.854 [main] DEBUG o.s.j.c.JdbcTemplate:643 - Executing prepared SQL statement [update account set balance = ? where name = ?]
2022-10-23 01:18:24.869 [main] DEBUG o.s.j.d.DataSourceTransactionManager:419 - Suspending current transaction
2022-10-23 01:18:24.873 [main] DEBUG o.s.j.c.JdbcTemplate:958 - Executing prepared SQL update
2022-10-23 01:18:24.873 [main] DEBUG o.s.j.c.JdbcTemplate:643 - Executing prepared SQL statement [update account set balance = ? where id = ?]
2022-10-23 01:18:24.873 [main] DEBUG o.s.j.d.DataSourceUtils:116 - Fetching JDBC Connection from DataSource
2022-10-23 01:18:24.879 [HikariPool-1 connection adder] DEBUG c.z.h.p.HikariPool:738 - HikariPool-1 - Added connection com.mysql.cj.jdbc.ConnectionImpl@6a7ec6dd
2022-10-23 01:18:24.885 [main] DEBUG o.s.j.d.DataSourceTransactionManager:996 - Resuming suspended transaction after completion of inner transaction
2022-10-23 01:18:24.885 [main] DEBUG o.s.j.d.DataSourceTransactionManager:740 - Initiating transaction commit
2022-10-23 01:18:24.886 [main] DEBUG o.s.j.d.DataSourceTransactionManager:330 - Committing JDBC transaction on Connection [HikariProxyConnection@666312528 wrapping com.mysql.cj.jdbc.ConnectionImpl@5b022357]
2022-10-23 01:18:24.887 [main] DEBUG o.s.j.d.DataSourceTransactionManager:389 - Releasing JDBC Connection [HikariProxyConnection@666312528 wrapping com.mysql.cj.jdbc.ConnectionImpl@5b022357] after transaction
```

这段日志大家认准这两句就行了：`Suspending current transaction` 表示挂起当前事务，`Resuming suspended transaction after completion of inner transaction` 表示恢复挂起的事务。

##### 4.2.2.7. NEVER

`NEVER` 表示以非事务方式运行，如果当前存在事务，则抛出异常。

假设 `handle2()` 方法有事务,`handle1()` 方法也有事务并且传播性为 `NEVER`，那么最终执行时会抛出如下异常：

```
org.springframework.transaction.IllegalTransactionStateException: Existing transaction found for transaction marked with propagation 'never'
```

### 4.3. 回滚规则

默认情况下，事务只有遇到运行时异常（`RuntimeException` 的子类）以及 `Error` 时才会回滚，在遇到检查型异常（`Checked Exception`）时不会回滚。

像 `1 / 0`，空指针这些是 `RuntimeException`，而 `IOException` 则算是 `Checked Exception`，换言之，默认情况下，如果发生 `IOException` 并不会导致事务回滚。

咱们如果希望发生 `IOException` 时也能触发事务回滚，那么可以按照如下方式配置：

- Java 配置

  ```java
  @Transactional(rollbackFor = IOException.class)
  public void handle2() {
      jdbcTemplate.update("update account set balance = ? where name = ?", 1, "javaboy");
      accountService.handle1();
  }
  ```

- XML 配置

  ```xml
  <tx:advice id="txAdvice" transaction-manager="transactionManager">
      <tx:attributes>
          <tx:method name="m3" rollback-for="java.io.IOException"/>
      </tx:attributes>
  </tx:advice>
  ```

另外，咱们也可以指定在发生某些异常时不回滚，例如当系统抛出 `ArithmeticException ` 异常时并不要触发事务回滚，则可以如下配置：

- Java 配置

  ```java
  @Transactional(noRollbackFor = ArithmeticException.class)
  public void handle2() {
      jdbcTemplate.update("update account set balance = ? where name = ?", 1, "javaboy");
      accountService.handle1();
  }
  ```

- XML 配置

  ```xml
  <tx:advice id="txAdvice" transaction-manager="transactionManager">
      <tx:attributes>
          <tx:method name="m3" no-rollback-for="java.lang.ArithmeticException"/>
      </tx:attributes>
  </tx:advice>
  ```

### 4.4. 是否只读

只读事务一般设置在查询方法上，但不是所有的查询方法都需要只读事务，要看具体情况。

一般来说，如果这个业务方法只有一个查询 SQL，那么就没必要添加事务，强行添加最终效果适得其反。

但是如果一个业务方法中有多个查询 SQL，情况就不一样了：多个查询 SQL，默认情况下，每个查询 SQL 都会开启一个独立的事务，这样，如果有并发操作修改了数据，那么多个查询 SQL 就会查到不一样的数据。此时，如果咱们开启事务，并设置为只读事务，那么多个查询 SQL 将被置于同一个事务中，多条相同的 SQL 在该事务中执行将会获取到相同的查询结果。

设置事务只读的方式如下：

- Java 配置

  ```java
  @Transactional(readOnly = true)
  ```

- XML 配置

  ```xml
  <tx:advice id="txAdvice" transaction-manager="transactionManager">
      <tx:attributes>
          <tx:method name="m3" read-only="true"/>
      </tx:attributes>
  </tx:advice>
  ```

### 4.5. 超时时间

超时时间是说一个事务允许执行的最长时间，如果超过该时间限制但事务还没有完成，则自动回滚事务。

事务超时时间配置方式如下（单位为秒）：

- Java 配置

  ```java
  @Transactional(timeout = 10)
  ```

- XML 配置

  ```xml
  <tx:advice id="txAdvice" transaction-manager="transactionManager">
      <tx:attributes>
          <tx:method name="m3" timeout="10"/>
      </tx:attributes>
  </tx:advice>
  ```

在 `TransactionDefinition` 中以 `int` 的值来表示超时时间，其单位是秒，默认值为 `-1`。

## 5. 注意事项

1. 事务只能应用到 `public` 方法上才会有效。
2. 事务需要从外部调用，**Spring 自调事务会失效**。即相同类里面，A 方法没有事务，B 方法有事务，A 方法调用 B 方法，则 B 方法的事务会失效，这点尤其要注意，因为代理模式只拦截通过代理传入的外部方法调用，所以自调用事务是不生效的。
3. 建议事务注解 `@Transactional` 一般添加在实现类上，而不要定义在接口上，如果加在接口类或接口方法上时，只有配置基于接口的代理这个注解才会生效。

## 6. 参考资料

- [长文捋明白 Spring 事务！隔离性？传播性？一网打尽！ - 江南一点雨 (javaboy.org)](http://www.javaboy.org/2021/1026/spring_transaction.html)