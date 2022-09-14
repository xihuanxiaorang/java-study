---
title: JDBC
tags: JDBC
created: 2022-08-24 18:12:20
modified: 2022-09-14 18:10:56
number headings: auto, first-level 1, max 6, _.1.1.
---

## 1. 简介

JDBC，全称是 Java DataBase Connectivity。  

- JDBC 即使用 Java 语言来访问 **关系型数据库** 的一套 API。
- **JDBC 是一种标准**，JDBC 标准提供的接口存在于 `java.sql` 包中。在这个包中定义有 **数据库的连接标准**、**数据库的操作标准** 以及 **数据库结果集的处理标准**。每个数据库厂商会提供各自的 JDBC 实现，程序员只需要面向接口和标准编程，不需要关心具体实现。  
![jdbc标准.drawio](attachments/jdbc标准.drawio.svg)  

| 接口/类           | 作用                                       |
| ----------------- | ------------------------------------------ |
| Driver            | 驱动接口                                   |
| DriverManager     | 工具类，用于管理驱动，可以获取数据库的链接 |
| Connection        | 表示 Java 与数据库建立的连接对象（接口）   |
| PreparedStatement | 发送 SQL 语句的工具                        |
| ResultSet         | 结果集，用于获取查询语句的结果             |

## 2. JDBC 执行流程

![JDBC 执行流程.excalidraw | 500](attachments/JDBC%20执行流程.excalidraw.svg)  

### 2.1. 环境搭建

> 本章节所涉及到的代码在 [GitHub - xihuanxiaorang/jdbc-study: 用于 jdbc 学习](https://github.com/xihuanxiaorang/jdbc-study) 仓库中，可以自行查看。  

执行以下 SQL 语句创建 `atguigudb` 数据库和 `user` 用户表，为后续的 JDBC 操作搭建一个测试环境。

```sql
CREATE DATABASE IF NOT EXISTS atguigudb;  
use atguigudb;  
CREATE TABLE IF NOT EXISTS `user`  (  
 `uid` BIGINT(32) AUTO_INCREMENT COMMENT '主键列(自动增长)',  
 `name` VARCHAR(32) NOT NULL COMMENT '用户名称',  
 `age` INT(3) NOT NULL COMMENT '用户年龄',  
 `birthday` DATE NOT NULL COMMENT '用户生日',  
 `salary` FLOAT DEFAULT 15000.0 COMMENT '用户月薪',  
 `note` TEXT COMMENT '用户说明',  
 CONSTRAINT pk_uid PRIMARY KEY (`uid`)  
) COMMENT '用户表';
```

### 2.2. 获取连接

#### 2.2.1. 驱动

`java.sql.Driver` 接口是所有驱动程序需要实现的接口。这个接口是提供给数据库厂商使用的，不同的数据库厂商提供不同的实现。其中，加载驱动由 Java SPI 机制实现，无需再像以前一样使用 `Class.forName("com.mysql.driver")` 来加载 MySQL 驱动。

> 对于 **Java SPI** 不清楚的小伙伴可以查看 [Java SPI](../../JDK/进阶/Java%20SPI/README.md) 这一篇文章，文章中详细地介绍了 Java SPI 机制的由来、原理以及应用。  

#### 2.2.2. URL

URL 用于标识一个被注册的驱动程序，从而建立到数据库的连接。URL 的标准由三部分组成，各部分之间用冒号分隔：

- 协议：java 的连接 URL 中的协议总是 jdbc
- 子协议：子协议用于标识一个数据库驱动程序
- 子名称：一种标识数据库的方法。子名称作用是为了 **定位数据库**。其包含 **主机名 (对应服务器的 ip 地址)**，**端口号**、**数据库名**  

MySQL 的连接 URL 编写方式：jdbc:mysql://主机名称:mysql 服务端口号/数据库名称?参数=值&参数=值。  
一个完整的 URL="**jdbc:mysql://localhost:3306/atguigudb?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai**"。

- `useUnicode=true&characterEncoding=utf-8` 的作用是指定字符的编码、解码格式。若 MySQL 数据库用到的是 GBK 的编码方式，而项目数据使用的是 UTF-8 的编码方式，这时如果添加了该参数则会在存取数据时根据 MySQL 和项目的编码方式将数据进行相应的格式转化。即：数据库在存项目数据时，会先将 UTF-8 格式数据解码成字节码，然后再将字节码重新使用 GBK 编码存到数据库中。在从数据库取数据时，会先将数据库中的数据按 GBK 格式解码成字节码，然后再将字节码重新按 UTF-8 格式编码数据，最后再将数据返回给客户端。
- MySQL5.7 之后要加上 `useSSL=false`，MySQL5.7 以及之前的版本则不用添加，默认为 false。`useSSL=true` 通过证书或者令牌进行安全验证，SSL 协议服务主要提供：认证用户服务器，确保数据发送到正确的服务器；加密数据，放置数据传输途中被窃取使用；维护数据完整性，验证数据在传输过程中是否丢失。
- MySQL8.0 之后必须加上 `serverTimezone=Asia/Shanghai`，指定当前服务器所处的时区。

#### 2.2.3. 用户名密码

建立数据库连接时必须的参数，其中的用户名和密码由自己保管，务必不要告诉他人。

#### 2.2.4. 测试

在资源目录 `resources` 下新建 `db.properties` 配置文件，用于维护数据库连接 URL、用户名和密码信息。

```properties
jdbc.url=jdbc:mysql://localhost:3306/atguigudb?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai  
jdbc.username=root  
jdbc.password=123456
```

编写 `JdbcTests` 测试类：

```java
public class JdbcTests {  
    private Connection connection;  
  
    @Before  
    public void before() throws IOException, SQLException {  
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("db.properties");  
        Properties properties = new Properties();  
        properties.load(inputStream);  
        String url = properties.getProperty("jdbc.url");  
        String username = properties.getProperty("jdbc.username");  
        String password = properties.getProperty("jdbc.password");  
        connection = DriverManager.getConnection(url, username, password);  
        System.out.println("【建立连接】：" + connection);  
    }  
  
    @After  
    public void after() throws SQLException {  
        if(connection != null) {  
            connection.close();  
            System.out.println("【" + connection +"连接关闭】");  
        }  
    }  
  
    @Test  
    public void testConnection() {  
  
    }   
}
```

测试结果如下所示：获取到 MySQL 连接实例对象，以及使用完成后及时关闭连接。`Connection` 的使用原则是 **尽量晚创建，尽量早释放**。  
![](attachments/Pasted%20image%2020220913172332.png)

### 2.3. Statement 接口

#### 2.3.1. 更新 - 添加数据

```java
@Test  
public void testAdd() throws SQLException {  
    Statement statement = connection.createStatement();  
    String sql = "INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES('小让', 18, '1995-07-13', 16000.0, '程序员');";  
    int count = statement.executeUpdate(sql);  
    System.out.println("【数据更新行数】：" + count);  
}
```

测试结果如下所示：【数据更新行数】：1  
![](attachments/Pasted%20image%2020220913172433.png)  
在 MySQL 客户端中执行 `select * from user;` 语句查看表中全部数据。  
![|700](attachments/Pasted%20image%2020220913172518.png)

#### 2.3.2. 更新 - 删除数据

```java
@Test  
public void testDelete() throws SQLException {  
    Statement statement = connection.createStatement();  
    String sql = "DELETE FROM `user` WHERE `uid` = 1;";  
    int count = statement.executeUpdate(sql);  
    System.out.println("【数据更新行数】：" + count);  
}
```

测试结果如下所示：【数据更新行数】：1  
![](attachments/Pasted%20image%2020220913172433.png)  
再次利用 MySQL 客户端执行 `select * from user;` 语句查看表中全部数据，发现刚刚插入进去的一条的数据已被成功删除。  
![|700](attachments/Pasted%20image%2020220913172721.png)

#### 2.3.3. 查询数据

在执行查询前先往 `user` 表中插入几条数据，这样可以保证等下查询出来的效果。

```sql
INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES('小让', 18, '1995-07-13', 16000.0, '程序员');  
INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES('小星', 18, '1995-03-20', 20000.0, '幼教');  
INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES('三十', 25, '1995-08-08', 22000.0, '硬件工程师');
```

编写测试代码：

```java
@Test  
public void testQuery() throws SQLException {  
    Statement statement = connection.createStatement();  
    String sql = "SELECT * FROM `user`;";  
    ResultSet rs = statement.executeQuery(sql);  
    while (rs.next()) {  
        int uid = rs.getInt("uid");  
        String name = rs.getString("name");  
        int age = rs.getInt("age");  
        Date birthday = rs.getDate("birthday");  
        float salary = rs.getFloat("salary");  
        String note = rs.getString("note");  
        System.out.println("User{" +  
                "uid=" + uid +  
                ", name='" + name + '\'' +  
                ", age=" + age +  
                ", birthday=" + birthday +  
                ", salary=" + salary +  
                ", note='" + note + '\'' +  
                '}');  
    }  
}
```

测试结果如下所示：  
![](attachments/Pasted%20image%2020220913174416.png)  
再次利用 MySQL 客户端执行 `select * from user;` 语句查看表中全部数据。  
![|700](attachments/Pasted%20image%2020220913174530.png)

#### 2.3.4. 💣SQL 注入

由于 Statement 使用的是拼接的 SQL 语句，所以很容易出现 SQL 注入问题。那么何为 SQL 注入呢？SQL 注入指的是某些系统没有对用户输入的数据进行充分的检查，而在用户输入数据中注入非法的 SQL 语句段或命令，从而利用系统的 SQL 引擎完成恶意行为的做法。  

咱们可以写一个简单的案例来测试一下：查询是否存在 ' 小白 ' 的用户，正常情况下用户表中是查询不到任何叫小白的用户的。

```java
@Test  
public void testSQLInjection() throws SQLException {  
    Statement statement = connection.createStatement();  
    String username = "'小白'";  
    String sql = "SELECT * FROM `user` where `name` = " + username;  
    ResultSet rs = statement.executeQuery(sql);  
    while (rs.next()) {  
        int uid = rs.getInt("uid");  
        String name = rs.getString("name");  
        int age = rs.getInt("age");  
        Date birthday = rs.getDate("birthday");  
        float salary = rs.getFloat("salary");  
        String note = rs.getString("note");  
        System.out.println("User{" +  
                "uid=" + uid +  
                ", name='" + name + '\'' +  
                ", age=" + age +  
                ", birthday=" + birthday +  
                ", salary=" + salary +  
                ", note='" + note + '\'' +  
                '}');  
    }  
}
```

测试结果如下所示：的确查询不到叫 ' 小白 ' 的用户。  
![](attachments/Pasted%20image%2020220913172332.png)  

但是此时，咱们将测试代码修改一下，让其中的 username = ' 小白 ' or 1 = 1

```java
@Test  
public void testSQLInjection() throws SQLException {  
    Statement statement = connection.createStatement();  
    String username = "'小白' or 1 = 1";  
    String sql = "SELECT * FROM `user` where `name` = " + username;  
    ResultSet rs = statement.executeQuery(sql);  
    while (rs.next()) {  
        int uid = rs.getInt("uid");  
        String name = rs.getString("name");  
        int age = rs.getInt("age");  
        Date birthday = rs.getDate("birthday");  
        float salary = rs.getFloat("salary");  
        String note = rs.getString("note");  
        System.out.println("User{" +  
                "uid=" + uid +  
                ", name='" + name + '\'' +  
                ", age=" + age +  
                ", birthday=" + birthday +  
                ", salary=" + salary +  
                ", note='" + note + '\'' +  
                '}');  
    }  
}
```

测试结果如下所示：可以查询到用户表中的全部数据。  
![](attachments/Pasted%20image%2020220913174416.png)

可以想象一下如果在登录系统的时候也使用 SQL 注入的手段，那么岂不是任何一个人无需用户名和密码都可以登录进系统，这是一件多么可怕的事情！那么有没有办法解决该问题呢？答案肯定是有的，此时就引出咱们即将学到的 `PreparedStatement` 接口。

### 2.4. PreparedStatement 接口

#### 2.4.1. MySQL 预编译

通常咱们发送一条 SQL 语句给 MySQL 服务器时，MySQL 服务器每次都需要对这条语句进行校验、解析等操作。如下图所示：  
![|800](attachments/Pasted%20image%2020220913234408.png)  
但是很多情况下，一条 SQL 语句可能需要反复的执行，每次执行可能仅仅是传递的参数不同而已，类似于这样的 SQL 语句如果每次都需要进行校验、解析等操作，未免太过于浪费性能，因此产生了 SQL 语句的预编译。所谓 **预编译** 就是将一些灵活的参数值以占位符 `?` 的形式给替代掉，把参数值给抽取出来，把 SQL 语句进行模板化。让 MySQL 服务器执行相同的 SQL 语句时，不再需要在校验、解析 SQL 语句上面花费重复的时间。  
如何使用预编译呢？

1. 定义预编译 SQL 语句

```mysql
prepare statement from 'select * from user where uid = ? and name = ?';
```

2. 设置参数值

```mysql
set @uid = 4,@name='小星';
```

3. 执行预编译 SQL 语句

```mysql
execute statement using @uid,@name;
```

运行结果如下所示：  
![|700](attachments/Pasted%20image%2020220913235732.png)

#### 2.4.2. 🔥PreparedStatement

可以通过 `Connection` 连接对象的 `prepareStatement(sql)` 方法获取 `PreparedStatement` 实例对象，其中，`PreparedStatement` 接口继承自 `Statement` 接口，方法中的参数 `sql` 表示一条预编译过的 SQL 语句，在 SQL 语句中的参数值用占位符 `?` 来表示，之后可以使用 `setXxx()` 或者 `setObject()` 方法来设置这些参数，需要注意的是💡，**索引值从 1 开始**。  

##### 2.4.2.1. 更新 - 添加数据

```java
@Test  
public void testPreparedStatementAdd() throws SQLException {  
    String sql = "INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES(?, ?, ?, ?, ?);";  
    PreparedStatement preparedStatement = connection.prepareStatement(sql);  
    preparedStatement.setString(1, "小白");  
    preparedStatement.setInt(2, 18);  
    preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));  
    preparedStatement.setFloat(4, 18000.0f);  
    preparedStatement.setString(5, "销售");  
    int count = preparedStatement.executeUpdate();  
    System.out.println("【数据更新行数】：" + count);  
}
```

测试结果如下所示：【数据更新行数】：1  
![](attachments/Pasted%20image%2020220913172433.png)  
在 MySQL 客户端中执行 `select * from user;` 语句查看表中全部数据。  
![|700](attachments/Pasted%20image%2020220914001850.png)

##### 2.4.2.2. 更新 - 删除数据

```java
@Test  
public void testPreparedStatementDelete() throws SQLException {  
    String sql = "DELETE FROM `user` WHERE `uid` = ?;";  
    PreparedStatement preparedStatement = connection.prepareStatement(sql);  
    preparedStatement.setInt(1, 6);  
    int count = preparedStatement.executeUpdate();  
    System.out.println("【数据更新行数】：" + count);  
}
```

测试结果如下所示：【数据更新行数】：1  
![](attachments/Pasted%20image%2020220913172433.png)  
在 MySQL 客户端中执行 `select * from user;` 语句查看表中全部数据，发现刚刚插入进去的一条的数据已被成功删除。  
![|700](attachments/Pasted%20image%2020220914002920.png)

##### 2.4.2.3. 查询数据

```java
@Test  
public void testPreparedStatementQuery() throws SQLException {  
    String sql = "SELECT * FROM `user`;";  
    PreparedStatement preparedStatement = connection.prepareStatement(sql);  
    ResultSet rs = preparedStatement.executeQuery();  
    while (rs.next()) {  
        int uid = rs.getInt("uid");  
        String name = rs.getString("name");  
        int age = rs.getInt("age");  
        Date birthday = rs.getDate("birthday");  
        float salary = rs.getFloat("salary");  
        String note = rs.getString("note");  
        System.out.println("User{" +  
                "uid=" + uid +  
                ", name='" + name + '\'' +  
                ", age=" + age +  
                ", birthday=" + birthday +  
                ", salary=" + salary +  
                ", note='" + note + '\'' +  
                '}');  
    }  
}
```

测试结果如下所示：  
![](attachments/Pasted%20image%2020220913174416.png)  
再次利用 MySQL 客户端执行 `select * from user;` 语句查看表中全部数据。  
![|700](attachments/Pasted%20image%2020220913174530.png)

#### 2.4.3. 💣问题

事实上，在使用 `PreparedStatement` 时默认是不能执行预编译的，需要在 URL 中增加额外参数 `useServerPrepStmts=true`（MySQL Server 4.1 之前的版本是不支持预编译的，而 MySQL Connector 在 5.0.5 以后的版本默认是不开启预编译功能的）。需要注意的是💡，当使用不同的 `PreparedStatement` 对象来执行相同的 SQL 语句时，还是会出现编译两次的现象，这是因为驱动没有缓存编译后的函数 key，会二次编译。如果希望缓存编译后函数的 key，那么就还需要增加一个参数 `cachePrepStmts=true`。URL 添加参数之后才能保证 MySQL 驱动先把 SQL 语句发送给服务器进行预编译，然后再执行 `executeQuery()` 时只是把参数发送给服务器。执行流程如下：  
![|600](attachments/Pasted%20image%2020220914020932.png)  
为了查看效果，不妨打开 MySQL 的通用查询日志：

```mysql
#查看general_log是否开启  
show variables like 'general_log%';  
#开启general log:  
set global general_log = 1;

#查询日志时区  
show variables like 'log_timestamps';  
#修改日志时区为系统默认的时区，如果想永久修改时区，则在my.ini配置文件中的[mysqld]下增加log_timestamps=SYSTEM  
set global log_timestamps=SYSTEM;

# 查看mysql数据存储目录  
show global variables like '%datadir%';
```

执行 `testPreparedStatementQuery()` 测试方法，查看 MySQL 数据存储目录下的 `general_log_file` 所对应的日志文件，发现执行的 SQL 语句依然是普通的 SQL 语句：  
![|900](attachments/Pasted%20image%2020220914021615.png)  
在 URL 上增加参数 `useServerPrepStmts=true&cachePrepStmts=true`，再次执行 `testPreparedStatementQuery()` 测试方法，再次查看日志文件，发现日志如下，确实成功开启预编译功能。  
![|900](attachments/Pasted%20image%2020220914022113.png)

#### 2.4.4. 防止 SQL 注入

使用 `PreparedStatement` 可以防止 SQL 注入，其根本原因就是 MySQL 已经对使用了占位符的 SQL 语句进行了预编译，执行计划中的条件已经确定，不能再额外添加其他条件，从而避免了 SQL 注入。  
咱们使用 `PreparedStatement` 的方式再来测试一下上面的 SQL 注入案例，看看是否可以查到名字叫 ' 小白 ' 的用户。

```java
@Test  
public void testPreparedStatementSQLInjection() throws SQLException {  
    String sql = "SELECT * FROM `user` where `name` = ?";  
    PreparedStatement preparedStatement = connection.prepareStatement(sql);  
    preparedStatement.setString(1, "'小白' or 1 = 1");  
    ResultSet rs = preparedStatement.executeQuery();  
    while (rs.next()) {  
        int uid = rs.getInt("uid");  
        String name = rs.getString("name");  
        int age = rs.getInt("age");  
        Date birthday = rs.getDate("birthday");  
        float salary = rs.getFloat("salary");  
        String note = rs.getString("note");  
        System.out.println("User{" +  
                "uid=" + uid +  
                ", name='" + name + '\'' +  
                ", age=" + age +  
                ", birthday=" + birthday +  
                ", salary=" + salary +  
                ", note='" + note + '\'' +  
                '}');  
    }  
}
```

测试结果如下所示：就是以 SQL 注入的方式也查询不到任何数据，成功！  
![](attachments/Pasted%20image%2020220913172332.png)  
查看日志可以发现，它把传入进行的参数值当成一个整体的字符串作为条件。  
![|900](attachments/Pasted%20image%2020220914023343.png)

## 3. 批处理

批处理允许将相关的 SQL 语句分组到一个批处理中，并通过一次调用将它们提交到数据库。当你一次向数据库发送多条 SQL 语句时，可以减少通信开销，从而提高性能。

- JDBC 驱动程序不一定支持该功能，可以使用 `DatabaseMataData.supportsBacthUpdates()` 方法来确定目标数据库是否支持批处理更新。如果 JDBC 驱动程序支持此功能，则该方法返回值为 true。

```java
@Test  
public void testSupportsBatchUpdates() throws SQLException {  
    DatabaseMetaData databaseMetaData = connection.getMetaData();  
    boolean supportsBatchUpdates = databaseMetaData.supportsBatchUpdates();  
    System.out.println("是否支持批处理？" + supportsBatchUpdates);  
}
```

运行测试代码，发现居然报错！  
![](attachments/Pasted%20image%2020220914140452.png)  
上网一查，发现 MySQL8.x 版本还需在 URL 上加上 `allowPublicKeyRetrieval=true` 参数。咱们加上，再试一次，发现 MySQL 是支持批处理功能的。  
![](attachments/Pasted%20image%2020220914140700.png)

- `Statement`、`PreparedStatement`、`CallableStatement` 的 `addBatch()` 方法用于将单个 SQL 语句添加到批处理中。
- `excuteBatch()` 方法用于执行所有放入批处理中的 SQL 语句。`excuteBatch()` 方法返回一个整数数组，数组中的每个元素代表各自更新语句的更新数目。
- 正如将 SQL 语句添加到批处理当中一样，可以使用 `clearBatch()` 方法清空，该方法用于清空所有添加到批处理当中的 SQL 语句，而无法指定要删除某条数据。

### 3.1. PreparedStatement 批处理

使用 `PreparedStatement` 实例对象进行批处理的典型步骤顺序如下：

1. 使用占位符创建 SQL 语句
2. 使用 `Connection` 实例对象的 `prepareStatement()` 方法获取 `PreparedStatement` 实例对象
3. 使用 `Connection` 实例对象的 `setAutoCommit(false)` 方法关闭自动提交，即取消自动提交事务 (在下面章节会详细介绍)。
4. 使用 `PreparedStatement` 实例对象的 `setXxx()` 方法给占位符赋值之后再使用 `addBatch()` 方法将 SQL 语句添加到批处理中
5. 使用 `PreparedStatement` 实例对象的 `executeBatch()` 方法执行批处理
6. 最后，使用 `Connection` 实例对象 `commit()` 方法提交所有的更改，或者出现异常时，使用 `rollback()` 方法回滚所有操作。

```java
@Test  
public void testPreparedStatementBatchAdd() throws SQLException {  
    connection.setAutoCommit(false);  
    try {  
        String sql = "INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES(?, ?, ?, ?, ?);";  
        PreparedStatement preparedStatement = connection.prepareStatement(sql);  
        for (int i = 0; i < 5; i++) {  
            preparedStatement.setString(1, "小白" + i);  
            preparedStatement.setInt(2, 18);  
            preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));  
            preparedStatement.setFloat(4, 18000.0f);  
            preparedStatement.setString(5, "销售");  
            preparedStatement.addBatch();  
        }  
        preparedStatement.executeBatch();  
        connection.commit();  
    } catch (SQLException e) {  
        e.printStackTrace();  
        connection.rollback();  
    }  
}
```

点击测试，发现插入还是挺快的，那么到底有没有用上批处理功能呢？咱们来查看一下 MySQL 日志信息，发现 SQL 语句还是一条一条发送的，并没有使用批处理功能！  
![|1000](attachments/Pasted%20image%2020220914143702.png)  

不卖关子了，其实还需要在 URL 中增加一个参数 `rewriteBatchedStatements=true`。  

> URL 上只有加上这个参数，并保证 MySQL 驱动在 5.1.13 以上版本，才能实现高性能的批量插入。MySQL 驱动在默认情况下会无视 `executeBatch()` 语句，把咱们期望批量执行的一组 SQL 语句拆散，一条一条地发给 MySQL 服务器，批量插入直接编程单条插入，所以造成较低的性能。另外，这个参数对 INSERT / UPDATE / DELETE 都有效。

咱们在 URL 上添加上该参数 `rewriteBatchedStatements=true` 后，再来测试一下，再看看 MySQL 的日志信息。惊讶地发现，程序居然报错了！  
![](attachments/Pasted%20image%2020220914145308.png)  
其实细心的小伙伴可以发现，在咱们的 SQL 语句最后有一个分号，这样在做批处理的时候就会出现上图中的错误，所以咱们需要把 SQL 语句最后的分号去掉。

```java
@Test  
public void testPreparedStatementBatchAdd() throws SQLException {  
    connection.setAutoCommit(false);  
    try {  
        String sql = "INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES(?, ?, ?, ?, ?)";  
        PreparedStatement preparedStatement = connection.prepareStatement(sql);  
        for (int i = 0; i < 5; i++) {  
            preparedStatement.setString(1, "小白" + i);  
            preparedStatement.setInt(2, 18);  
            preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));  
            preparedStatement.setFloat(4, 18000.0f);  
            preparedStatement.setString(5, "销售");  
            preparedStatement.addBatch();  
        }  
        preparedStatement.executeBatch();  
        connection.commit();  
    } catch (SQLException e) {  
        e.printStackTrace();  
        connection.rollback();  
    }  
}
```

再来测试一把，发现执行成功，此时再来看看 MySQL 的日志信息，发现达到预期效果！  
![](attachments/Pasted%20image%2020220914145741.png)

### 3.2. 优化

由于 JDBC 批处理利用的是 SQL 中 `INSERT INTO ... VALUES` 的方式插入多条数据，所以当以这种方式插入大量的 (几百万或者几千万) 数据时，可能会出现如下异常：

```text
com.mysql.cj.jdbc.exceptions.PacketTooBigException: Packet for query is too large (99,899,527 > 67,108,864). You can change this value on the server by setting the 'max_allowed_packet' variable.
```

`max_allowed_packet` 为数据包消息缓存区最大大小，单位为字节，默认值为 67108864（64M），最大值 1073741824（1G），最小值 1024（1K），参数值须为 1024 的倍数，非倍数将四舍五入到最接近的倍数。数据包消息缓存区初始大小为 `net_buffer_length` 个字节，每条 SQL 语句和它的参数都会产生一个数据包消息缓存区，跟事务无关。  
如何查看与设置 `max_allowed_packet` 参数？

```sql
# 查看数据包消息缓存区初始大小  
show variables like 'net_buffer_length';  
# 查看数据包消息缓存区最大大小  
show variables like 'max_allowed_packet';
# 重新打开数据库连接参数生效，数据库服务重启后参数恢复为默认，想永久修改的话，则在my.ini配置文件中的[mysqld]下增加max_allowed_packet=32*1024*1024  
set global max_allowed_packet=32*1024*1024;
```

咱们为了测试效果，将该值设置小一点，`set global max_allowed_packet=20*1024*10;` 设置成 200K 大小之后，编写测试代码。

```java
@Test  
public void testPreparedStatementBatchAdd2() throws SQLException {  
    connection.setAutoCommit(false);  
    try {  
        String sql = "INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES(?, ?, ?, ?, ?)";  
        PreparedStatement preparedStatement = connection.prepareStatement(sql);  
        for (int i = 0; i < 1000000; i++) {  
            preparedStatement.setString(1, "小白" + i);  
            preparedStatement.setInt(2, 18);  
            preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));  
            preparedStatement.setFloat(4, 18000.0f);  
            preparedStatement.setString(5, "销售");  
            preparedStatement.addBatch();  
        }  
        preparedStatement.executeBatch();  
        connection.commit();  
    } catch (SQLException e) {  
        e.printStackTrace();  
        connection.rollback();  
    }  
}
```

插入一百万条数据，点击测试，发现程序报错，报错的信息就和咱们上面提到的一样。  
![](attachments/Pasted%20image%2020220914154524.png)  
那么该怎么优化代码呢？其实很简单，咱们分批次处理，一次处理 500 条数据，代码优化如下：

```java
@Test  
public void testPreparedStatementBatchAdd3() throws SQLException {  
    long start = System.currentTimeMillis();  
    connection.setAutoCommit(false);  
    try {  
        String sql = "INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES(?, ?, ?, ?, ?)";  
        PreparedStatement preparedStatement = connection.prepareStatement(sql);  
        for (int i = 1; i <= 1000000; i++) {  
            preparedStatement.setString(1, "小白" + i);  
            preparedStatement.setInt(2, 18);  
            preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));  
            preparedStatement.setFloat(4, 18000.0f);  
            preparedStatement.setString(5, "销售");  
            preparedStatement.addBatch();  
            if (i % 500 == 0) {  
                preparedStatement.executeBatch();  
                preparedStatement.clearBatch();  
            }  
        }  
        preparedStatement.executeBatch();  
        preparedStatement.clearBatch();  
        connection.commit();  
    } catch (SQLException e) {  
        e.printStackTrace();  
        connection.rollback();  
    }  
    System.out.println("百万条数据插入用时：" + (System.currentTimeMillis() - start) + "【单位：毫秒】");  
}
```

点击测试，等待一段时间后，发现插入成功！测试结果如下所示：  
![](attachments/Pasted%20image%2020220914160454.png)  
查看数据库中用户表的数据，发现已全部成功插入！

## 4. 事务

事务是指作为单个逻辑工作单元执行的一系列操作，这些操作要么一起成功，要么一起失败，是一个不可分割的工作单元。  
说到事务最典型的案例就是转账了：

> 张三要给李四转账 500 块钱，这里涉及到两个操作，从张三的账户上减去 500 块钱，给李四的账户添加 500 块钱，这两个操作要么同时成功要么同时失败，如何确保他们同时成功或者同时失败呢？答案就是事务。

事务具有四大特性：  原子性 (Atomicity)、一致性 (Consistency)、隔离性 (Isolation)、持久性 (Durability) 4 个特性，这 4 个特征也被称为 ACID 特性。

- 原子性：一个事务（transaction）中的所有操作，要么全部完成，要么全部不完成，不会结束在中间某个环节。事务在执行过程中发生错误，会被回滚（Rollback）到事务开始前的状态，就像这个事务从来没有执行过一样。即，事务不可分割、不可约简。
- 一致性：指的是在数据库操作前后是完全一致的，保证数据的有效性，如果事务正常操作则系统会维持有效性，如果事务出现了错误，则回到最原始状态，也要维持其有效性，这样保证事务开始时和结束时系统处于一致状态。如在转账业务中，如果张三给李四转账成功，则保持其一致性，张三的钱减少，李四的钱增加；如果现在转账失败，则保持操作之前的一致性，即 张三的钱不会减少，李四的钱不会增加。
- 隔离性：数据库允许多个并发事务同时对其数据进行读写和修改，隔离性可以防止多个事务并发执行时由于交叉执行而导致数据的不一致。事务隔离分为不同级别，包括未提交读（Read Uncommitted）、提交读（Read Committed）、可重复读（Repeatable Read）和串行化（Serializable）。
- 持久性：事务处理结束后，对数据的修改就是永久的，即便系统故障也不会丢失。

### 4.1. MySQL 对事务的支持

在 MySQL 中提供了如下表中所示的几个命令，可以进行事务的处理。

| 序号 | 命令                             | 描述                                   |
| ---- | -------------------------------- | -------------------------------------- |
| 1    | SET AUTOCOMMIT = 0               | 取消自动提交处理，开启事务处理         |
| 2    | SET AUTOCOMMIT = 1               | 打开自动提交处理，关闭事务处理         |
| 3    | START TRANSACTION                | 启动事务                            |
| 4    | BEGIN                            | 启动事务，相当于执行 START TRANSACTION |
| 5    | COMMIT                           | 提交事务                               |
| 6    | ROLLBACK                         | 回滚全部操作                           |
| 7    | SAVEPOINT 事务保存点名称         | 设置事务保存点                         |
| 8    | ROLLBACK TO SAVEPOINT 保存点名称 | 回滚操作到保存点                       |

以上所有操作都是针对于一个 `session` 的，在数据库操作中把每一个连接到次数据库上的用户都称为一个 `session`。在 MySQL 中，如果要应用事务处理，则应该按照以下顺序输入命令：

1. 取消自动提交，执行 `SET AUTOCOMMIT = 0`，这样所有的更新指令并不会立刻发送到数据表中，而只存在于当前 `session`。
2. 开启事务，执行 `START TRANSACTION` 或者 `BEGIN`。
3. 编写数据库更新语句，如增加、修改、删除，可以在编写的更新语句之间记录事务的保存点，使用 `SAVEPOINT` 指令。
4. 提交事务，如果确信数据库的修改没有任何的错误，则使用 `COMMIT` 提交事务，在提交之前对数据库所有的全部操作都将保存在 `session` 中。
5. 事务回滚，如果发现执行的 SQL 语句有错误，则使用 `ROLLBACK` 命令全部撤销，或者使用 `ROLLBACK TO SAVEPOINT` 记录点，让其回滚到指定的位置。

当一个事务进行修改时，其他的 `session` 是无法看到此事务的操作状态的。即此 `session` 对数据库所有的一切修改，如果没有提交事务，则其他 `session` 是无法看到此 `session` 操作结果的。

### 4.2. JDBC 事务处理

在 JDBC 中的事务是使用 `Connection` 实例对象的 `commit()` 方法和 `rollback()` 方法来管理的。在 JDBC 中事务的默认提交时机，如下：

- 当一个连接对象被创建时，**默认情况下是自动提交事务**，每次执行一条 SQL 语句时，如果执行成功，就会向数据库自动提交，此操作不能回滚。
- 关闭数据库连接，数据就会自动提交。如果多个操作，每个操作使用的是自己单独的连接 (Connection)，则无法保证事务。**同一个事务的多个操作必须在同一个连接下**。  

在 JDBC 中使用事务的基本步骤如下：

1. 调用 `Connection` 实例对象的 `setAutoCommit(false)` 方法以取消自动提交事务。
2. 在所有的 SQL 语句都成功执行后，调用 `commit()` 方法提交事务。
3. 在出现异常时，调用 `rollback()` 方法回滚事务。
4. 若此时 `Connection` 没有被关闭，还可能被重复使用，则需要恢复其自动提交状态 `setAutoCommit(true)`。

### 4.3. 测试

>为了方便观察运行效果，下面的操作每次执行前都将删除已有的 user 表，并重新创建 user 表，这样自动编号将从 1 开始。然后插入三条初始数据。

```mysql
INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES('小让', 18, '1995-07-13', 16000.0, '程序员');  
INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES('小星', 18, '1995-03-20', 20000.0, '幼教');  
INSERT INTO `user`(`name`, `age`, `birthday`, `salary`, `note`) VALUES('三十', 25, '1995-08-08', 22000.0, '硬件工程师');
```

可以看到用户 1 的薪资为 16000，用户 2 的薪资为 20000，现在咱们就模拟一个场景，让用户 1 的薪资减 1000，然后让用户 2 的薪资加 1000，两该过程作为一个整体，总的薪资应该不变，其实就是模拟的转账过程。

#### 4.3.1. 没有事务的情况

```java
@Test  
public void testTransferNonTransaction() {  
    try {  
        String sql1 = "UPDATE `user` SET `salary` = `salary` - ? WHERE `uid` = ?;";  
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);  
        preparedStatement1.setFloat(1, 1000.0f);  
        preparedStatement1.setInt(2, 1);  
        preparedStatement1.executeUpdate();  
        int i = 1 / 0;  
        String sql2 = "UPDATE `user` SET `salary` = `salary` + ? WHERE `uid` = ?;";  
        PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);  
        preparedStatement2.setFloat(1, 1000.0f);  
        preparedStatement2.setInt(2, 2);  
        preparedStatement2.executeUpdate();  
    } catch (Exception e) {  
        e.printStackTrace();  
    }  
}
```

测试结果如下所示：出现异常。  
![](attachments/Pasted%20image%2020220914175607.png)  
然后查看数据库用户表中的数据：  
![|700](attachments/Pasted%20image%2020220914174811.png)  
发现在没有事务并且出现异常的情况下，用户 1 已经扣了 1000，但是用户 2 并没有加 1000，此时就暴露出没有事务的危险性。

#### 4.3.2. 存在事务的情况

```java
@Test  
public void testTransferWithTransaction() throws SQLException {  
    connection.setAutoCommit(false);  
    try {  
        String sql1 = "UPDATE `user` SET `salary` = `salary` - ? WHERE `uid` = ?;";  
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);  
        preparedStatement1.setFloat(1, 1000.0f);  
        preparedStatement1.setInt(2, 1);  
        preparedStatement1.executeUpdate();  
        int i = 1 / 0;  
        String sql2 = "UPDATE `user` SET `salary` = `salary` + ? WHERE `uid` = ?;";  
        PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);  
        preparedStatement2.setFloat(1, 1000.0f);  
        preparedStatement2.setInt(2, 2);  
        preparedStatement2.executeUpdate();  
        connection.commit();  
    } catch (Exception e) {  
        e.printStackTrace();  
        connection.rollback();  
    }  
}
```

测试结果如下所示：出现异常。  
![](attachments/Pasted%20image%2020220914175607.png)  
然后查看数据库用户表中的数据：  
![|700](attachments/Pasted%20image%2020220914175802.png)  
发现在增加事务之后，即使在出现异常的情况下，也不会发生用户 1 已经扣 1000，而用户 2 没有加钱的尴尬情况。所以说事务控制非常有比要。

## 5. 数据库连接池

待续。。。
