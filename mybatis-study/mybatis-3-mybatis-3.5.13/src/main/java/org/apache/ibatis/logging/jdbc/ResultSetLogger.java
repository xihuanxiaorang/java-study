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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

/**
 * ResultSet proxy to add logging.
 *
 * @author Clinton Begin
 * @author Eduardo Macarron
 */
public final class ResultSetLogger extends BaseJdbcLogger implements InvocationHandler {
  private static final Set<Integer> BLOB_TYPES = new HashSet<>();

  static {
    BLOB_TYPES.add(Types.BINARY);
    BLOB_TYPES.add(Types.BLOB);
    BLOB_TYPES.add(Types.CLOB);
    BLOB_TYPES.add(Types.LONGNVARCHAR);
    BLOB_TYPES.add(Types.LONGVARBINARY);
    BLOB_TYPES.add(Types.LONGVARCHAR);
    BLOB_TYPES.add(Types.NCLOB);
    BLOB_TYPES.add(Types.VARBINARY);
  }

  /**
   * 原始 ResultSet 对象
   */
  private final ResultSet rs;
  private final Set<Integer> blobColumns = new HashSet<>();
  private boolean first = true;
  private int rows;

  private ResultSetLogger(ResultSet rs, Log statementLog, int queryStack) {
    super(statementLog, queryStack);
    this.rs = rs;
  }

  /**
   * Creates a logging version of a ResultSet.
   *
   * @param rs           the ResultSet to proxy
   * @param statementLog the statement log
   * @param queryStack   the query stack
   * @return the ResultSet with logging
   */
  public static ResultSet newInstance(ResultSet rs, Log statementLog, int queryStack) {
    InvocationHandler handler = new ResultSetLogger(rs, statementLog, queryStack);
    ClassLoader cl = ResultSet.class.getClassLoader();
    // 创建 ResultSet 结果集对象的代理对象
    return (ResultSet) Proxy.newProxyInstance(cl, new Class[]{ResultSet.class}, handler);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
    try {
      // 如果当前方法是 Object 类中的方法，则直接调用，不做其他任何处理
      if (Object.class.equals(method.getDeclaringClass())) {
        return method.invoke(this, params);
      }
      // 直接调用原始 ResultSet 对象中对应的方法
      Object o = method.invoke(rs, params);
      // 针对 next 方法进行后置处理
      if ("next".equals(method.getName())) {
        // 检测 next 方法的返回值，确定是否还有下一行数据，如果方法返回 true 的话，则打印 ResultSet 结果集中的列名称和每一行数据
        if ((Boolean) o) {
          // 记录结果集中的行数
          rows++;
          // 如果开启 TRACE 日志，则打印 ResultSet 结果集中的列名称和每一行数据
          if (isTraceEnabled()) {
            // 获取 ResultSet 结果集中的列元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // 获取 ResultSet 结果集中的列数量
            final int columnCount = rsmd.getColumnCount();
            if (first) {
              first = false;
              // 打印 ResultSet 结果集中的列名称，格式类似于：   Columns: id, name, age
              printColumnHeaders(rsmd, columnCount);
            }
            // 打印 ResultSet 结果集中的每一行数据，格式类似于：       Row: 1, 张三, 18
            printColumnValues(columnCount);
          }
        } else {
          // 如果 next 方法返回 false，则表示没有下一行数据了，打印总共查询到的行数，格式类似于：     Total: 1
          debug("     Total: " + rows, false);
        }
      }
      // 清空 column* 集合
      clearColumnInfo();
      return o;
    } catch (Throwable t) {
      throw ExceptionUtil.unwrapThrowable(t);
    }
  }

  private void printColumnHeaders(ResultSetMetaData rsmd, int columnCount) throws SQLException {
    StringJoiner row = new StringJoiner(", ", "   Columns: ", "");
    for (int i = 1; i <= columnCount; i++) {
      if (BLOB_TYPES.contains(rsmd.getColumnType(i))) {
        blobColumns.add(i);
      }
      row.add(rsmd.getColumnLabel(i));
    }
    trace(row.toString(), false);
  }

  private void printColumnValues(int columnCount) {
    StringJoiner row = new StringJoiner(", ", "       Row: ", "");
    for (int i = 1; i <= columnCount; i++) {
      try {
        if (blobColumns.contains(i)) {
          row.add("<<BLOB>>");
        } else {
          row.add(rs.getString(i));
        }
      } catch (SQLException e) {
        // generally can't call getString() on a BLOB column
        row.add("<<Cannot Display>>");
      }
    }
    trace(row.toString(), false);
  }

  /**
   * Get the wrapped result set.
   *
   * @return the resultSet
   */
  public ResultSet getRs() {
    return rs;
  }

}
