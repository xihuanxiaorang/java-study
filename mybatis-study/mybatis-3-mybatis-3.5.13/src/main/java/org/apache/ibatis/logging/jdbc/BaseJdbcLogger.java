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

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.reflection.ArrayUtil;

import java.lang.reflect.Method;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Base class for proxies to do logging.
 *
 * @author Clinton Begin
 * @author Eduardo Macarron
 */
public abstract class BaseJdbcLogger {

  /**
   * 存储 PreparedStatement 中所有 set 方法的名称，例如：setString、setInt、setLong 等
   * 作用：用于判断当前方法是否为 PreparedStatement 中的 set 方法
   */
  protected static final Set<String> SET_METHODS;

  /**
   * 存储 PreparedStatement 中所有 execute 方法的名称，例如：execute、executeUpdate、executeQuery、addBatch 等
   * 作用：用于判断当前方法是否为 PreparedStatement 中的 execute 方法
   */
  protected static final Set<String> EXECUTE_METHODS = new HashSet<>();

  static {
    SET_METHODS = Arrays.stream(PreparedStatement.class.getDeclaredMethods())
      .filter(method -> method.getName().startsWith("set")).filter(method -> method.getParameterCount() > 1)
      .map(Method::getName).collect(Collectors.toSet());

    EXECUTE_METHODS.add("execute");
    EXECUTE_METHODS.add("executeUpdate");
    EXECUTE_METHODS.add("executeQuery");
    EXECUTE_METHODS.add("addBatch");
  }

  /**
   * 日志对象，用于记录日志，
   * 使用适配器模式将不同的日志框架进行统一，例如：Log4j、Log4j2、Logback、JDK Logging 等
   */
  protected final Log statementLog;
  protected final int queryStack;

  /**
   * 用于存储 PreparedStatement 中的 set 方法的参数（占位符），其中 Key = 参数的索引位置，Value = 参数的值
   */
  private final Map<Object, Object> columnMap = new HashMap<>();

  /**
   * 用于存储 PreparedStatement 中的 set 方法的参数名称（即参数的索引位置），例如：setString(1, "张三") 中的 1
   */
  private final List<Object> columnNames = new ArrayList<>();

  /**
   * 用于存储 PreparedStatement 中的 set 方法的参数值，例如：setString(1, "张三") 中的 "张三"
   */
  private final List<Object> columnValues = new ArrayList<>();

  /*
   * Default constructor
   */
  public BaseJdbcLogger(Log log, int queryStack) {
    this.statementLog = log;
    if (queryStack == 0) {
      this.queryStack = 1;
    } else {
      this.queryStack = queryStack;
    }
  }

  protected void setColumn(Object key, Object value) {
    columnMap.put(key, value);
    columnNames.add(key);
    columnValues.add(value);
  }

  protected Object getColumn(Object key) {
    return columnMap.get(key);
  }

  protected String getParameterValueString() {
    List<Object> typeList = new ArrayList<>(columnValues.size());
    for (Object value : columnValues) {
      if (value == null) {
        typeList.add("null");
      } else {
        typeList.add(objectValueString(value) + "(" + value.getClass().getSimpleName() + ")");
      }
    }
    final String parameters = typeList.toString();
    return parameters.substring(1, parameters.length() - 1);
  }

  protected String objectValueString(Object value) {
    if (value instanceof Array) {
      try {
        return ArrayUtil.toString(((Array) value).getArray());
      } catch (SQLException e) {
        // Intentialy fall through to return value.toString()
      }
    }
    return value.toString();
  }

  protected String getColumnString() {
    return columnNames.toString();
  }

  protected void clearColumnInfo() {
    columnMap.clear();
    columnNames.clear();
    columnValues.clear();
  }

  protected String removeExtraWhitespace(String original) {
    return SqlSourceBuilder.removeExtraWhitespaces(original);
  }

  protected boolean isDebugEnabled() {
    return statementLog.isDebugEnabled();
  }

  protected boolean isTraceEnabled() {
    return statementLog.isTraceEnabled();
  }

  protected void debug(String text, boolean input) {
    if (statementLog.isDebugEnabled()) {
      statementLog.debug(prefix(input) + text);
    }
  }

  protected void trace(String text, boolean input) {
    if (statementLog.isTraceEnabled()) {
      statementLog.trace(prefix(input) + text);
    }
  }

  private String prefix(boolean isInput) {
    char[] buffer = new char[queryStack * 2 + 2];
    Arrays.fill(buffer, '=');
    buffer[queryStack * 2 + 1] = ' ';
    if (isInput) {
      buffer[queryStack * 2] = '>';
    } else {
      buffer[0] = '<';
    }
    return new String(buffer);
  }

}
