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
package org.apache.ibatis.datasource.unpooled;

import org.apache.ibatis.io.Resources;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * @author Clinton Begin
 * @author Eduardo Macarron
 */
public class UnpooledDataSource implements DataSource {

  /**
   * 用于缓存所有已注册的数据库连接驱动
   */
  private static final Map<String, Driver> registeredDrivers = new ConcurrentHashMap<>();

  static {
    // 获取所有已在 DriverManager 中注册的 JDBC 驱动器实例
    Enumeration<Driver> drivers = DriverManager.getDrivers();
    while (drivers.hasMoreElements()) {
      Driver driver = drivers.nextElement();
      // 将已注册的 JDBC 驱动器实例复制一份到当前类的 registeredDrivers 集合中
      registeredDrivers.put(driver.getClass().getName(), driver);
    }
  }

  /**
   * 数据库驱动类加载器
   */
  private ClassLoader driverClassLoader;
  /**
   * 数据库连接驱动的相关配置
   */
  private Properties driverProperties;
  /**
   * 数据库驱动类名
   */
  private String driver;
  /**
   * 数据库连接地址
   */
  private String url;
  /**
   * 数据库用户名
   */
  private String username;
  /**
   * 数据库密码
   */
  private String password;
  /**
   * 是否自动提交
   */
  private Boolean autoCommit;
  /**
   * 默认的事务隔离级别
   */
  private Integer defaultTransactionIsolationLevel;
  /**
   * 默认的连接超时时长
   */
  private Integer defaultNetworkTimeout;

  public UnpooledDataSource() {
  }

  public UnpooledDataSource(String driver, String url, String username, String password) {
    this.driver = driver;
    this.url = url;
    this.username = username;
    this.password = password;
  }

  public UnpooledDataSource(String driver, String url, Properties driverProperties) {
    this.driver = driver;
    this.url = url;
    this.driverProperties = driverProperties;
  }

  public UnpooledDataSource(ClassLoader driverClassLoader, String driver, String url, String username,
                            String password) {
    this.driverClassLoader = driverClassLoader;
    this.driver = driver;
    this.url = url;
    this.username = username;
    this.password = password;
  }

  public UnpooledDataSource(ClassLoader driverClassLoader, String driver, String url, Properties driverProperties) {
    this.driverClassLoader = driverClassLoader;
    this.driver = driver;
    this.url = url;
    this.driverProperties = driverProperties;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return doGetConnection(username, password);
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return doGetConnection(username, password);
  }

  @Override
  public int getLoginTimeout() {
    return DriverManager.getLoginTimeout();
  }

  @Override
  public void setLoginTimeout(int loginTimeout) {
    DriverManager.setLoginTimeout(loginTimeout);
  }

  @Override
  public PrintWriter getLogWriter() {
    return DriverManager.getLogWriter();
  }

  @Override
  public void setLogWriter(PrintWriter logWriter) {
    DriverManager.setLogWriter(logWriter);
  }

  public ClassLoader getDriverClassLoader() {
    return driverClassLoader;
  }

  public void setDriverClassLoader(ClassLoader driverClassLoader) {
    this.driverClassLoader = driverClassLoader;
  }

  public Properties getDriverProperties() {
    return driverProperties;
  }

  public void setDriverProperties(Properties driverProperties) {
    this.driverProperties = driverProperties;
  }

  public synchronized String getDriver() {
    return driver;
  }

  public synchronized void setDriver(String driver) {
    this.driver = driver;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

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

  public Boolean isAutoCommit() {
    return autoCommit;
  }

  public void setAutoCommit(Boolean autoCommit) {
    this.autoCommit = autoCommit;
  }

  public Integer getDefaultTransactionIsolationLevel() {
    return defaultTransactionIsolationLevel;
  }

  public void setDefaultTransactionIsolationLevel(Integer defaultTransactionIsolationLevel) {
    this.defaultTransactionIsolationLevel = defaultTransactionIsolationLevel;
  }

  /**
   * Gets the default network timeout.
   *
   * @return the default network timeout
   * @since 3.5.2
   */
  public Integer getDefaultNetworkTimeout() {
    return defaultNetworkTimeout;
  }

  /**
   * Sets the default network timeout value to wait for the database operation to complete. See
   * {@link Connection#setNetworkTimeout(java.util.concurrent.Executor, int)}
   *
   * @param defaultNetworkTimeout The time in milliseconds to wait for the database operation to complete.
   * @since 3.5.2
   */
  public void setDefaultNetworkTimeout(Integer defaultNetworkTimeout) {
    this.defaultNetworkTimeout = defaultNetworkTimeout;
  }

  private Connection doGetConnection(String username, String password) throws SQLException {
    Properties props = new Properties();
    if (driverProperties != null) {
      props.putAll(driverProperties);
    }
    if (username != null) {
      props.setProperty("user", username);
    }
    if (password != null) {
      props.setProperty("password", password);
    }
    return doGetConnection(props);
  }

  private Connection doGetConnection(Properties properties) throws SQLException {
    // 初始化数据库驱动，如果当前驱动未注册，则进行注册
    initializeDriver();
    // 通过 DriverManager 获取数据库连接
    Connection connection = DriverManager.getConnection(url, properties);
    // 配置数据库连接，包括连接超时时长、事务是否自动提交以及事务隔离级别
    configureConnection(connection);
    return connection;
  }

  /**
   * 初始化驱动
   *
   * @throws SQLException 如果初始化驱动失败，则抛出 SQLException 异常
   */
  private synchronized void initializeDriver() throws SQLException {
    // 如果配置中指定的驱动未注册，则进行注册，如果已经注册的话，则直接返回
    if (!registeredDrivers.containsKey(driver)) {
      Class<?> driverType;
      try {
        if (driverClassLoader != null) {
          // 如果 driverClassLoader 不为空，则通过 driverClassLoader 加载驱动
          driverType = Class.forName(driver, true, driverClassLoader);
        } else {
          // 通过其他 ClassLoader 加载驱动
          driverType = Resources.classForName(driver);
        }
        // DriverManager requires the driver to be loaded via the system ClassLoader.
        // https://www.kfu.com/~nsayer/Java/dyn-jdbc.html
        // 通过反射创建 Driver 对象
        Driver driverInstance = (Driver) driverType.getDeclaredConstructor().newInstance();
        // 创建 DriverProxy 对象，并注册到 DriverManager 中
        DriverManager.registerDriver(new DriverProxy(driverInstance));
        // 将当前驱动实例添加到 registeredDrivers 集合中
        registeredDrivers.put(driver, driverInstance);
      } catch (Exception e) {
        throw new SQLException("Error setting driver on UnpooledDataSource. Cause: " + e);
      }
    }
  }

  /**
   * 配置数据库连接，包括连接超时时长、事务是否自动提交以及事务隔离级别
   *
   * @param conn 数据库连接
   * @throws SQLException 如果配置数据库连接失败，则抛出 SQLException 异常
   */
  private void configureConnection(Connection conn) throws SQLException {
    if (defaultNetworkTimeout != null) {
      conn.setNetworkTimeout(Executors.newSingleThreadExecutor(), defaultNetworkTimeout);
    }
    if (autoCommit != null && autoCommit != conn.getAutoCommit()) {
      conn.setAutoCommit(autoCommit);
    }
    if (defaultTransactionIsolationLevel != null) {
      conn.setTransactionIsolation(defaultTransactionIsolationLevel);
    }
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new SQLException(getClass().getName() + " is not a wrapper.");
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }

  @Override
  public Logger getParentLogger() {
    // requires JDK version 1.6
    return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  }

  private static class DriverProxy implements Driver {
    private final Driver driver;

    DriverProxy(Driver d) {
      this.driver = d;
    }

    @Override
    public boolean acceptsURL(String u) throws SQLException {
      return this.driver.acceptsURL(u);
    }

    @Override
    public Connection connect(String u, Properties p) throws SQLException {
      return this.driver.connect(u, p);
    }

    @Override
    public int getMajorVersion() {
      return this.driver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
      return this.driver.getMinorVersion();
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
      return this.driver.getPropertyInfo(u, p);
    }

    @Override
    public boolean jdbcCompliant() {
      return this.driver.jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() {
      return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }
  }

}
