---
title: Spring-JDBC
tags: spring jdbc
created: 2022-08-25 21:15:39
modified: 2022-09-18 21:32:59
number headings: auto, first-level 1, max 6, _.1.1.
---

## 1. 环境搭建

> 本章节所涉及到的代码在 [GitHub - xihuanxiaorang/spring-study: 用于spring学习](https://github.com/xihuanxiaorang/spring-study) 仓库中的 `jdbc` 模块，可以自行查看。

### 1.1. 创建 spring-study-jdbc 模块

选中项目右键新建一个模块，模块名填自己喜欢的即可，这里我就填 `spring-study-jdbc`，最后点击确定即可。

![|1189](attachments/Pasted%20image%2020221021084810.png)

### 1.2. 引入相关依赖

在模块的 `pom.xml` 配置文件中引入以下依赖：`MySQL` 驱动程序、`HikariCP` 数据库连接池以及 `spring-jdbc`。

```xml
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <dependency>
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
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

### 1.4. 数据源配置

数据库连接池配置 `db.properties`：

```properties
# 【JDBC】数据库驱动程序
jdbc.driverClassName=com.mysql.cj.jdbc.Driver
# 【JDBC】数据库连接地址
jdbc.url=jdbc:mysql://localhost:3306/atguigudb?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
# 【JDBC】数据库连接用户名
jdbc.username=root
# 【JDBC】数据库连接密码
jdbc.password=123456
# 【HikariCP】配置数据库连接超时时间（单位：毫秒）
jdbc.connectionTimeout=3000
# 【HikariCP】配置当前数据库是否为只读
jdbc.readOnly=false
# 【HikariCP】配置一个连接最小的维持时间（单位：毫秒）
jdbc.pool.idleTimeout=3000
# 【HikariCP】配置一个连接最长的存活时间（单位：毫秒）
jdbc.pool.maxLifetime=6000
# 【HikariCP】数据库连接池提供的最大连接数
jdbc.pool.maxPoolSize=60
# 【HikariCP】数据库连接池在没有任何用户访问时，最少维持的连接数
jdbc.pool.minIdle=20
```

将数据库连接池配置信息封装到 `HikariCPProperties` 类中：

```java
@Component
@PropertySource(value = {"classpath:db.properties"})
@Data
public class HikariCPProperties {
    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    @Value("${jdbc.connectionTimeout}")
    private long connectionTimeout;
    @Value("${jdbc.readOnly}")
    private boolean isReadOnly;
    @Value("${jdbc.pool.idleTimeout}")
    private long idleTimeout;
    @Value("${jdbc.pool.maxLifetime}")
    private long maxLifetime;
    @Value("${jdbc.pool.maxPoolSize}")
    private int maxPoolSize;
    @Value("${jdbc.pool.minIdle}")
    private int minIdle;
}
```

向 Spring 容器中注入数据源组件：

```java
@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource(HikariCPProperties properties) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setJdbcUrl(properties.getJdbcUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setConnectionTimeout(properties.getConnectionTimeout());
        dataSource.setReadOnly(properties.isReadOnly());
        dataSource.setIdleTimeout(properties.getIdleTimeout());
        dataSource.setMaxLifetime(properties.getMaxLifetime());
        dataSource.setMaximumPoolSize(properties.getMaxPoolSize());
        dataSource.setMinimumIdle(properties.getMinIdle());
        return dataSource;
    }
}
```

### 1.5. 添加数据库测试数据

```mysql
CREATE TABLE IF NOT EXISTS `account`
(
    `id`      INT UNSIGNED       NOT NULL AUTO_INCREMENT,
    `name`    VARCHAR(40) UNIQUE NOT NULL,
    `balance` INT UNSIGNED       NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO account(`name`, `balance`)
VALUES ('javaboy', 1000);
INSERT INTO account(`name`, `balance`)
VALUES ('itboyhub', 1000);
```

### 1.6. 主配置类

扫描模块下的所有组件，以及向 Spring 容器中注入一个 `JdbcTemplate` 组件。

```java
@Configuration
@ComponentScan(basePackages = {"top.xiaorang.spring.jdbc"})
public class MainConfig {
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
```



