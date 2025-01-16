package fun.xiaorang.study.java.core.spi.jdbc;

import org.junit.jupiter.api.Test;

import java.sql.*;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/16 19:09
 */
public class ApiTest {
  private static final String URL = "jdbc:mysql://localhost:3306/test";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "123456";

  @Test
  public void test() throws ClassNotFoundException {
    // 加载驱动（使用 Class.forName 方法）
    Class.forName("com.mysql.cj.jdbc.Driver");
    // 使用 try-catch-resources 语句块来确保资源被正确关闭
    try (
            // 打开数据库连接
            final Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            // 创建 Statement 对象
            final Statement statement = connection.createStatement();
            // 执行查询
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM tb_user")) {
      // 处理查询结果
      while (resultSet.next()) {
        System.out.println(resultSet.getString("name"));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
