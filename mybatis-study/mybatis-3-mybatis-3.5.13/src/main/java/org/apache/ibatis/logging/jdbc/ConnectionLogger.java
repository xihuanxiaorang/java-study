/*
 *    Copyright 2009-2023 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.logging.jdbc;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.reflection.ExceptionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Connection proxy to add logging.
 *
 * @author Clinton Begin
 * @author Eduardo Macarron
 */
public final class ConnectionLogger extends BaseJdbcLogger implements InvocationHandler {

  /**
   * 真正的 Connection 数据库连接对象
   */
  private final Connection connection;

  private ConnectionLogger(Connection conn, Log statementLog, int queryStack) {
    super(statementLog, queryStack);
    this.connection = conn;
  }

  /**
   * 创建并返回 Connection 数据库连接对象的代理对象
   * 这样在调用 Connection 对象中的方法时，就会进行日志记录
   *
   * @param conn         真正的 Connection 数据库连接对象
   * @param statementLog StatementLog 日志对象
   * @param queryStack   查询堆栈
   * @return Connection 数据库连接对象的代理对象，带有日志记录功能
   */
  public static Connection newInstance(Connection conn, Log statementLog, int queryStack) {
    InvocationHandler handler = new ConnectionLogger(conn, statementLog, queryStack);
    ClassLoader cl = Connection.class.getClassLoader();
    // 创建 Connection 数据库连接对象的代理对象
    return (Connection) Proxy.newProxyInstance(cl, new Class[]{Connection.class}, handler);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
    try {
      // 如果当前方法是 Object 类中的方法，则直接调用，不做其他任何处理
      if (Object.class.equals(method.getDeclaringClass())) {
        return method.invoke(this, params);
      }
      // 如果当前方法是 Connection 中的 prepareStatement 或 prepareCall 方法，则进行日志记录
      if ("prepareStatement".equals(method.getName()) || "prepareCall".equals(method.getName())) {
        if (isDebugEnabled()) {
          // 如果开启 DEBUG 日志，则打印 SQL 语句，格式类似于：==>  Preparing: select * from user where id = ?
          debug(" Preparing: " + removeExtraWhitespace((String) params[0]), true);
        }
        // 使用 Connection 数据库连接对象创建 PreparedStatement 对象
        PreparedStatement stmt = (PreparedStatement) method.invoke(connection, params);
        // 创建并返回 PreparedStatement 数据库操作对象的代理对象，这样在调用 PreparedStatement 对象中的方法时，就会进行日志记录
        return PreparedStatementLogger.newInstance(stmt, statementLog, queryStack);
      }
      // 如果当前方法是 Connection 中的 createStatement 方法
      if ("createStatement".equals(method.getName())) {
        // 使用 Connection 数据库连接对象创建 Statement 对象
        Statement stmt = (Statement) method.invoke(connection, params);
        // 创建并返回 Statement 数据库操作对象的代理对象，这样在调用 Statement 对象中的方法时，就会进行日志记录
        return StatementLogger.newInstance(stmt, statementLog, queryStack);
      } else {
        // 如果当前方法是 Connection 中的其他方法，则直接调用
        return method.invoke(connection, params);
      }
    } catch (Throwable t) {
      throw ExceptionUtil.unwrapThrowable(t);
    }
  }

  /**
   * return the wrapped connection.
   *
   * @return the connection
   */
  public Connection getConnection() {
    return connection;
  }

}
