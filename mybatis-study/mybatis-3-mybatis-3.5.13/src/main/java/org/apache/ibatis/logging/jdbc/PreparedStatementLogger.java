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
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * PreparedStatement proxy to add logging.
 *
 * @author Clinton Begin
 * @author Eduardo Macarron
 */
public final class PreparedStatementLogger extends BaseJdbcLogger implements InvocationHandler {

  /**
   * 原始的 PreparedStatement 对象
   */
  private final PreparedStatement statement;

  private PreparedStatementLogger(PreparedStatement stmt, Log statementLog, int queryStack) {
    super(statementLog, queryStack);
    this.statement = stmt;
  }

  /**
   * Creates a logging version of a PreparedStatement.
   *
   * @param stmt         - the statement
   * @param statementLog - the statement log
   * @param queryStack   - the query stack
   * @return - the proxy
   */
  public static PreparedStatement newInstance(PreparedStatement stmt, Log statementLog, int queryStack) {
    InvocationHandler handler = new PreparedStatementLogger(stmt, statementLog, queryStack);
    ClassLoader cl = PreparedStatement.class.getClassLoader();
    // 创建 PreparedStatement 数据库操作对象的代理对象
    return (PreparedStatement) Proxy.newProxyInstance(cl,
      new Class[]{PreparedStatement.class, CallableStatement.class}, handler);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
    try {
      // 如果当前方法是 Object 类中的方法，则直接调用，不做其他任何处理
      if (Object.class.equals(method.getDeclaringClass())) {
        return method.invoke(this, params);
      }
      // 如果当前方法是 PreparedStatement 接口中的 execute、executeUpdate、executeQuery、addBatch 方法，则进行日志记录
      if (EXECUTE_METHODS.contains(method.getName())) {
        if (isDebugEnabled()) {
          // 如果开启 DEBUG 日志，则打印参数信息，格式类似于：==>  Parameters: 1(String)
          debug("Parameters: " + getParameterValueString(), true);
        }
        // 清空 column* 集合
        clearColumnInfo();
        if ("executeQuery".equals(method.getName())) {
          // 如果当前方法是 executeQuery 方法，则会调用原始 PreparedStatement 对象中的 executeQuery 方法，得到 ResultSet 结果集对象
          ResultSet rs = (ResultSet) method.invoke(statement, params);
          // 如果 ResultSet 结果集对象不为空，则创建并返回 ResultSet 结果集对象的代理对象，这样在调用 ResultSet 对象中的方法时，就会进行日志记录
          return rs == null ? null : ResultSetLogger.newInstance(rs, statementLog, queryStack);
        } else {
          // 如果当前方法是其他三个方法 execute、executeUpdate、addBatch 中的一个，则直接调用原始 PreparedStatement 对象中对应的方法
          return method.invoke(statement, params);
        }
      }
      // 如果当前方法是 PreparedStatement 接口中的 set 方法，则进行参数记录
      if (SET_METHODS.contains(method.getName())) {
        if ("setNull".equals(method.getName())) {
          setColumn(params[0], null);
        } else {
          // 记录参数信息，第一个参数是 key = 参数的索引位置，第二个参数是 value = 参数的值
          setColumn(params[0], params[1]);
        }
        // 直接调用原始 PreparedStatement 对象的 set 方法
        return method.invoke(statement, params);
      } else if ("getResultSet".equals(method.getName())) {
        // 如果当前方法是 getResultSet 方法，则调用原始 PreparedStatement 对象的 getResultSet 方法，得到 ResultSet 结果集对象
        ResultSet rs = (ResultSet) method.invoke(statement, params);
        // 如果 ResultSet 结果集对象不为空，则创建并返回 ResultSet 结果集对象的代理对象，这样在调用 ResultSet 对象中的方法时，就会进行日志记录
        return rs == null ? null : ResultSetLogger.newInstance(rs, statementLog, queryStack);
      } else if ("getUpdateCount".equals(method.getName())) {
        // 如果当前方法是 getUpdateCount 方法，则调用原始 PreparedStatement 对象的 getUpdateCount 方法，得到更新的行数
        int updateCount = (Integer) method.invoke(statement, params);
        if (updateCount != -1) {
          // 如果更新的行数不为 -1，则打印更新的行数，格式类似于：   Updates: 1
          debug("   Updates: " + updateCount, false);
        }
        // 返回更新的行数
        return updateCount;
      } else {
        // 如果当前方法是 PreparedStatement 接口中的其他方法，则直接调用原始 PreparedStatement 对象中对应的方法
        return method.invoke(statement, params);
      }
    } catch (Throwable t) {
      throw ExceptionUtil.unwrapThrowable(t);
    }
  }

  /**
   * Return the wrapped prepared statement.
   *
   * @return the PreparedStatement
   */
  public PreparedStatement getPreparedStatement() {
    return statement;
  }

}
