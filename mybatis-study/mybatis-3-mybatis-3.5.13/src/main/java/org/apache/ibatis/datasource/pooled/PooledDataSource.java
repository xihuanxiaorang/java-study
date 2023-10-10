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
package org.apache.ibatis.datasource.pooled;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * This is a simple, synchronous, thread-safe database connection pool.
 *
 * @author Clinton Begin
 */
public class PooledDataSource implements DataSource {

  private static final Log log = LogFactory.getLog(PooledDataSource.class);

  /**
   * PoolState 对象，记录池化的状态，比如：总连接数、活跃连接数、空闲连接数等
   */
  private final PoolState state = new PoolState(this);

  /**
   * UnpooledDataSource 对象，记录了数据库的基本信息，比如：驱动类、连接地址、用户名、密码等
   */
  private final UnpooledDataSource dataSource;
  /**
   * 可重入锁，用于对 state 对象的修改操作进行控制
   */
  private final Lock lock = new ReentrantLock();
  /**
   * 条件变量，当线程获取不到连接时，可以阻塞，等待其它线程释放连接
   */
  private final Condition condition = lock.newCondition();
  // OPTIONAL CONFIGURATION FIELDS
  /**
   * 最大活跃连接数
   */
  protected int poolMaximumActiveConnections = 10;
  /**
   * 最大空闲连接数
   */
  protected int poolMaximumIdleConnections = 5;
  /**
   * 连接最大执行时间
   */
  protected int poolMaximumCheckoutTime = 20000;
  /**
   * 等待连接的时间
   */
  protected int poolTimeToWait = 20000;
  /**
   * 最大允许的坏连接数
   */
  protected int poolMaximumLocalBadConnectionTolerance = 3;
  /**
   * 发送到数据库的侦测查询，用来检验连接是否正常工作并准备接受请求
   */
  protected String poolPingQuery = "NO PING QUERY SET";
  protected boolean poolPingEnabled;
  protected int poolPingConnectionsNotUsedFor;
  private int expectedConnectionTypeCode;

  public PooledDataSource() {
    dataSource = new UnpooledDataSource();
  }

  public PooledDataSource(UnpooledDataSource dataSource) {
    this.dataSource = dataSource;
  }

  public PooledDataSource(String driver, String url, String username, String password) {
    dataSource = new UnpooledDataSource(driver, url, username, password);
    expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(),
      dataSource.getPassword());
  }

  public PooledDataSource(String driver, String url, Properties driverProperties) {
    dataSource = new UnpooledDataSource(driver, url, driverProperties);
    expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(),
      dataSource.getPassword());
  }

  public PooledDataSource(ClassLoader driverClassLoader, String driver, String url, String username, String password) {
    dataSource = new UnpooledDataSource(driverClassLoader, driver, url, username, password);
    expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(),
      dataSource.getPassword());
  }

  public PooledDataSource(ClassLoader driverClassLoader, String driver, String url, Properties driverProperties) {
    dataSource = new UnpooledDataSource(driverClassLoader, driver, url, driverProperties);
    expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(),
      dataSource.getPassword());
  }

  /**
   * Unwraps a pooled connection to get to the 'real' connection
   *
   * @param conn - the pooled connection to unwrap
   * @return The 'real' connection
   */
  public static Connection unwrapConnection(Connection conn) {
    if (Proxy.isProxyClass(conn.getClass())) {
      InvocationHandler handler = Proxy.getInvocationHandler(conn);
      if (handler instanceof PooledConnection) {
        return ((PooledConnection) handler).getRealConnection();
      }
    }
    return conn;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return popConnection(dataSource.getUsername(), dataSource.getPassword()).getProxyConnection();
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return popConnection(username, password).getProxyConnection();
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

  public void setDefaultAutoCommit(boolean defaultAutoCommit) {
    dataSource.setAutoCommit(defaultAutoCommit);
    forceCloseAll();
  }

  public String getDriver() {
    return dataSource.getDriver();
  }

  public void setDriver(String driver) {
    dataSource.setDriver(driver);
    forceCloseAll();
  }

  public String getUrl() {
    return dataSource.getUrl();
  }

  public void setUrl(String url) {
    dataSource.setUrl(url);
    forceCloseAll();
  }

  public String getUsername() {
    return dataSource.getUsername();
  }

  public void setUsername(String username) {
    dataSource.setUsername(username);
    forceCloseAll();
  }

  public String getPassword() {
    return dataSource.getPassword();
  }

  public void setPassword(String password) {
    dataSource.setPassword(password);
    forceCloseAll();
  }

  public boolean isAutoCommit() {
    return dataSource.isAutoCommit();
  }

  public Integer getDefaultTransactionIsolationLevel() {
    return dataSource.getDefaultTransactionIsolationLevel();
  }

  public void setDefaultTransactionIsolationLevel(Integer defaultTransactionIsolationLevel) {
    dataSource.setDefaultTransactionIsolationLevel(defaultTransactionIsolationLevel);
    forceCloseAll();
  }

  public Properties getDriverProperties() {
    return dataSource.getDriverProperties();
  }

  public void setDriverProperties(Properties driverProps) {
    dataSource.setDriverProperties(driverProps);
    forceCloseAll();
  }

  /**
   * Gets the default network timeout.
   *
   * @return the default network timeout
   * @since 3.5.2
   */
  public Integer getDefaultNetworkTimeout() {
    return dataSource.getDefaultNetworkTimeout();
  }

  /**
   * Sets the default network timeout value to wait for the database operation to complete. See
   * {@link Connection#setNetworkTimeout(java.util.concurrent.Executor, int)}
   *
   * @param milliseconds The time in milliseconds to wait for the database operation to complete.
   * @since 3.5.2
   */
  public void setDefaultNetworkTimeout(Integer milliseconds) {
    dataSource.setDefaultNetworkTimeout(milliseconds);
    forceCloseAll();
  }

  public int getPoolMaximumActiveConnections() {
    return poolMaximumActiveConnections;
  }

  /**
   * The maximum number of active connections.
   *
   * @param poolMaximumActiveConnections The maximum number of active connections
   */
  public void setPoolMaximumActiveConnections(int poolMaximumActiveConnections) {
    this.poolMaximumActiveConnections = poolMaximumActiveConnections;
    forceCloseAll();
  }

  public int getPoolMaximumIdleConnections() {
    return poolMaximumIdleConnections;
  }

  /**
   * The maximum number of idle connections.
   *
   * @param poolMaximumIdleConnections The maximum number of idle connections
   */
  public void setPoolMaximumIdleConnections(int poolMaximumIdleConnections) {
    this.poolMaximumIdleConnections = poolMaximumIdleConnections;
    forceCloseAll();
  }

  public int getPoolMaximumLocalBadConnectionTolerance() {
    return poolMaximumLocalBadConnectionTolerance;
  }

  /**
   * The maximum number of tolerance for bad connection happens in one thread which are applying for new
   * {@link PooledConnection}.
   *
   * @param poolMaximumLocalBadConnectionTolerance max tolerance for bad connection happens in one thread
   * @since 3.4.5
   */
  public void setPoolMaximumLocalBadConnectionTolerance(int poolMaximumLocalBadConnectionTolerance) {
    this.poolMaximumLocalBadConnectionTolerance = poolMaximumLocalBadConnectionTolerance;
  }

  public int getPoolMaximumCheckoutTime() {
    return poolMaximumCheckoutTime;
  }

  /**
   * The maximum time a connection can be used before it *may* be given away again.
   *
   * @param poolMaximumCheckoutTime The maximum time
   */
  public void setPoolMaximumCheckoutTime(int poolMaximumCheckoutTime) {
    this.poolMaximumCheckoutTime = poolMaximumCheckoutTime;
    forceCloseAll();
  }

  public int getPoolTimeToWait() {
    return poolTimeToWait;
  }

  /**
   * The time to wait before retrying to get a connection.
   *
   * @param poolTimeToWait The time to wait
   */
  public void setPoolTimeToWait(int poolTimeToWait) {
    this.poolTimeToWait = poolTimeToWait;
    forceCloseAll();
  }

  public String getPoolPingQuery() {
    return poolPingQuery;
  }

  /**
   * The query to be used to check a connection.
   *
   * @param poolPingQuery The query
   */
  public void setPoolPingQuery(String poolPingQuery) {
    this.poolPingQuery = poolPingQuery;
    forceCloseAll();
  }

  public boolean isPoolPingEnabled() {
    return poolPingEnabled;
  }

  /**
   * Determines if the ping query should be used.
   *
   * @param poolPingEnabled True if we need to check a connection before using it
   */
  public void setPoolPingEnabled(boolean poolPingEnabled) {
    this.poolPingEnabled = poolPingEnabled;
    forceCloseAll();
  }

  public int getPoolPingConnectionsNotUsedFor() {
    return poolPingConnectionsNotUsedFor;
  }

  /**
   * If a connection has not been used in this many milliseconds, ping the database to make sure the connection is still
   * good.
   *
   * @param milliseconds the number of milliseconds of inactivity that will trigger a ping
   */
  public void setPoolPingConnectionsNotUsedFor(int milliseconds) {
    this.poolPingConnectionsNotUsedFor = milliseconds;
    forceCloseAll();
  }

  /**
   * Closes all active and idle connections in the pool.
   */
  public void forceCloseAll() {
    lock.lock();
    try {
      expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(),
        dataSource.getPassword());
      for (int i = state.activeConnections.size(); i > 0; i--) {
        try {
          PooledConnection conn = state.activeConnections.remove(i - 1);
          conn.invalidate();

          Connection realConn = conn.getRealConnection();
          if (!realConn.getAutoCommit()) {
            realConn.rollback();
          }
          realConn.close();
        } catch (Exception e) {
          // ignore
        }
      }
      for (int i = state.idleConnections.size(); i > 0; i--) {
        try {
          PooledConnection conn = state.idleConnections.remove(i - 1);
          conn.invalidate();

          Connection realConn = conn.getRealConnection();
          if (!realConn.getAutoCommit()) {
            realConn.rollback();
          }
          realConn.close();
        } catch (Exception e) {
          // ignore
        }
      }
    } finally {
      lock.unlock();
    }
    if (log.isDebugEnabled()) {
      log.debug("PooledDataSource forcefully closed/removed all connections.");
    }
  }

  public PoolState getPoolState() {
    return state;
  }

  private int assembleConnectionTypeCode(String url, String username, String password) {
    return (url + username + password).hashCode();
  }

  /**
   * 归还连接
   * <p> 1. 从活跃连接集合中移除传入的连接对象 </p>
   * <p> 2. 检测连接是否有效？
   * <p> 2.1. 如果有效，则检测空闲连接数是否已达到上限？ </p>
   * <p> 2.1.1. 如果未达到上限，则将该连接重新封装成 PooledConnection 对象，并添加到空闲连接池中；然后唤醒阻塞等待的线程，通知已有空闲连接 </p>
   * <p> 2.1.2. 如果已达到上限，则直接关闭真正的数据库连接 </p>
   * <p> 2.2. 如果无效，则将无效连接数 +1 </p>
   *
   * @param conn 连接，类型为 PooledConnection
   * @throws SQLException 归还连接失败，则抛出 SQLException 异常
   */
  protected void pushConnection(PooledConnection conn) throws SQLException {
    // 获取锁，同步代码块，保证线程安全，防止多个线程同时操作 state 对象，导致数据不一致，出现问题
    lock.lock();
    try {
      // 从活跃连接集合中移除指定的连接
      state.activeConnections.remove(conn);
      // 检测连接是否有效
      if (conn.isValid()) {
        // 检测空闲连接数是否已达到上限
        if (state.idleConnections.size() < poolMaximumIdleConnections
          && conn.getConnectionTypeCode() == expectedConnectionTypeCode) {
          // 累计连接的使用时长
          state.accumulatedCheckoutTime += conn.getCheckoutTime();
          // 如果连接未设置自动提交，则进行回滚操作
          if (!conn.getRealConnection().getAutoCommit()) {
            conn.getRealConnection().rollback();
          }
          // 将底层连接重新封装成 PooledConnection 对象，并添加到空闲连接集合中
          PooledConnection newConn = new PooledConnection(conn.getRealConnection(), this);
          state.idleConnections.add(newConn);
          // 设置新连接的创建时间和最后使用时间
          newConn.setCreatedTimestamp(conn.getCreatedTimestamp());
          newConn.setLastUsedTimestamp(conn.getLastUsedTimestamp());
          // 将旧的连接标记为无效状态
          conn.invalidate();
          if (log.isDebugEnabled()) {
            log.debug("Returned connection " + newConn.getRealHashCode() + " to pool.");
          }
          // 唤醒阻塞等待的线程，通知其已有空闲连接
          condition.signal();
        } else {
          // 统计连接的使用时长
          state.accumulatedCheckoutTime += conn.getCheckoutTime();
          // 如果连接未设置自动提交，则进行回滚操作
          if (!conn.getRealConnection().getAutoCommit()) {
            conn.getRealConnection().rollback();
          }
          // 直接关闭真正的数据库连接
          conn.getRealConnection().close();
          if (log.isDebugEnabled()) {
            log.debug("Closed connection " + conn.getRealHashCode() + ".");
          }
          // 将旧的连接标记为无效状态
          conn.invalidate();
        }
      } else {
        if (log.isDebugEnabled()) {
          log.debug("A bad connection (" + conn.getRealHashCode()
            + ") attempted to return to the pool, discarding connection.");
        }
        // 无效连接数 +1
        state.badConnectionCount++;
      }
    } finally {
      // 释放锁
      lock.unlock();
    }
  }

  /**
   * 尝试从空闲连接池中获取可用连接
   * <p> 1. 检测当前连接池中是否有空闲的有效连接？</p>
   * <p> 1.1. 如果有，则直接返回连接；</p>
   * <p> 1.2. 如果没有，则继续执行下一步。 </p>
   * <p> 2. 检查连接池当前的活跃连接数是否已经达到上限值？</p>
   * <p> 2.1. 如果未达到，则尝试创建一个新的数据库连接，并在创建成功之后，返回新建的连接；</p>
   * <p> 2.2. 如果已达到最大上限，则往下执行。 </p>
   * <p> 3. 从活跃连接中获取最早的一个连接，检测该连接是否使用超时？</p>
   * <p> 3.1. 如果未超时，则必须进行阻塞等待，直至等待超时或者有其他线程释放连接；</p>
   * <p> 3.1.1. 如果等待超时，则抛出 SQLException 异常，结束循环；</p>
   * <p> 3.1.2. 如果等待期间，有连接被释放，则会被唤醒，继续执行上述步骤。 </p>
   * <p> 3.2. 如果该连接使用超时的话，则将该连接标记为无效状态，从活跃连接中移除，并创建一个新的数据库连接，返回新建的连接。 </p>
   *
   * @param username 用户名
   * @param password 密码
   * @return 可用的连接，类型为 PooledConnection
   * @throws SQLException 创建连接失败，则抛出 SQLException 异常
   */
  private PooledConnection popConnection(String username, String password) throws SQLException {
    boolean countedWait = false;
    PooledConnection conn = null;
    long t = System.currentTimeMillis();
    int localBadConnectionCount = 0;

    // 循环，直到获取到可用的连接，或者抛出异常
    while (conn == null) {
      /*
        获取锁，同步代码块，保证线程安全，防止多个线程同时操作 state 对象，导致数据不一致，出现问题，
        比如：超过最大连接数，但是还是创建了新的连接，导致连接数超过最大连接数
       */
      lock.lock();
      try {
        // 如果当前连接池中有空闲的连接，则直接从空闲连接池中获取一个连接
        if (!state.idleConnections.isEmpty()) {
          // Pool has available connection
          conn = state.idleConnections.remove(0);
          if (log.isDebugEnabled()) {
            log.debug("Checked out connection " + conn.getRealHashCode() + " from pool.");
          }
        }
        // 如果当前连接池中没有空闲的连接，且活跃连接数未达到最大连接数，则创建一个新的连接
        else if (state.activeConnections.size() < poolMaximumActiveConnections) {
          // Pool does not have available connection and can create a new connection
          conn = new PooledConnection(dataSource.getConnection(), this);
          if (log.isDebugEnabled()) {
            log.debug("Created connection " + conn.getRealHashCode() + ".");
          }
        }
        /*
          如果当前连接池中没有空闲的连接，且活跃连接数已达到最大连接数，则不能创建新的连接，只能从活跃连接中获取一个连接，
          或者等待，直到有连接被释放，或者等待超时，抛出异常，结束循环
         */
        else {
          // Cannot create new connection
          // 获取最早的一个活跃连接
          PooledConnection oldestActiveConnection = state.activeConnections.get(0);
          // 获取该连接的使用时长
          long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
          // 检测该连接是否使用超时，如果超时，则将该连接标记为无效状态，然后创建一个新的连接，返回新建的连接
          if (longestCheckoutTime > poolMaximumCheckoutTime) {
            // Can claim overdue connection
            // 对超时连接的统计
            state.claimedOverdueConnectionCount++;
            state.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
            state.accumulatedCheckoutTime += longestCheckoutTime;
            // 从活跃连接中移除超时连接
            state.activeConnections.remove(oldestActiveConnection);
            // 如果超时连接未提交，则进行回滚
            if (!oldestActiveConnection.getRealConnection().getAutoCommit()) {
              try {
                oldestActiveConnection.getRealConnection().rollback();
              } catch (SQLException e) {
                /*
                 * Just log a message for debug and continue to execute the following statement like nothing happened.
                 * Wrap the bad connection with a new PooledConnection, this will help to not interrupt current
                 * executing thread and give current thread a chance to join the next competition for another valid/good
                 * database connection. At the end of this loop, bad {@link @conn} will be set as null.
                 */
                log.debug("Bad connection. Could not roll back");
              }
            }
            // 创建一个新的连接，返回新建的连接
            conn = new PooledConnection(oldestActiveConnection.getRealConnection(), this);
            conn.setCreatedTimestamp(oldestActiveConnection.getCreatedTimestamp());
            conn.setLastUsedTimestamp(oldestActiveConnection.getLastUsedTimestamp());
            // 标记超时连接为无效状态
            oldestActiveConnection.invalidate();
            if (log.isDebugEnabled()) {
              log.debug("Claimed overdue connection " + conn.getRealHashCode() + ".");
            }
          } else {
            // Must wait
            try {
              // 对等待连接的统计
              if (!countedWait) {
                state.hadToWaitCount++;
                countedWait = true;
              }
              if (log.isDebugEnabled()) {
                log.debug("Waiting as long as " + poolTimeToWait + " milliseconds for connection.");
              }
              long wt = System.currentTimeMillis();
              // 等待指定的时间，直到超时，或者有连接被释放
              condition.await(poolTimeToWait, TimeUnit.MILLISECONDS);
              // 统计等待连接的时间
              state.accumulatedWaitTime += System.currentTimeMillis() - wt;
            } catch (InterruptedException e) {
              // set interrupt flag
              Thread.currentThread().interrupt();
              break;
            }
          }
        }
        // 如果获取到连接，则对连接进行初始化操作
        if (conn != null) {
          // ping to server and check the connection is valid or not
          // 检测连接是否有效
          if (conn.isValid()) {
            // 如果连接有效，并且未自动提交，则进行回滚操作
            if (!conn.getRealConnection().getAutoCommit()) {
              conn.getRealConnection().rollback();
            }
            // 设置连接的相关属性
            conn.setConnectionTypeCode(assembleConnectionTypeCode(dataSource.getUrl(), username, password));
            conn.setCheckoutTimestamp(System.currentTimeMillis());
            conn.setLastUsedTimestamp(System.currentTimeMillis());
            // 将连接添加到活跃连接集合中
            state.activeConnections.add(conn);
            state.requestCount++;
            state.accumulatedRequestTime += System.currentTimeMillis() - t;
          } else {
            if (log.isDebugEnabled()) {
              log.debug("A bad connection (" + conn.getRealHashCode()
                + ") was returned from the pool, getting another connection.");
            }
            // 对无效连接的统计
            state.badConnectionCount++;
            localBadConnectionCount++;
            conn = null;
            // 如果无效连接数超过最大允许的坏连接数，则抛出异常，结束循环
            if (localBadConnectionCount > poolMaximumIdleConnections + poolMaximumLocalBadConnectionTolerance) {
              if (log.isDebugEnabled()) {
                log.debug("PooledDataSource: Could not get a good connection to the database.");
              }
              throw new SQLException("PooledDataSource: Could not get a good connection to the database.");
            }
          }
        }
      } finally {
        // 释放锁
        lock.unlock();
      }

    }

    // 如果获取不到连接，则抛出异常！
    if (conn == null) {
      if (log.isDebugEnabled()) {
        log.debug("PooledDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
      }
      throw new SQLException(
        "PooledDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
    }

    return conn;
  }

  /**
   * Method to check to see if a connection is still usable
   *
   * @param conn - the connection to check
   * @return True if the connection is still usable
   */
  protected boolean pingConnection(PooledConnection conn) {
    // 标识位，记录连接是否仍然有效
    boolean result = true;

    try {
      // 检测真实的数据库连接是否已经关闭，如果已经关闭，则返回 false
      result = !conn.getRealConnection().isClosed();
    } catch (SQLException e) {
      if (log.isDebugEnabled()) {
        log.debug("Connection " + conn.getRealHashCode() + " is BAD: " + e.getMessage());
      }
      result = false;
    }

    /*
      如果连接未关闭并且开启侦测查询，
      另外，ping 操作不能频繁执行，只有超过一定时长 (超过 poolPingConnectionsNotUsedFor 指定的时长)未使用的连接，才需要 ping 操作来检测数据库连接是否正常
     */
    if (result && poolPingEnabled && poolPingConnectionsNotUsedFor >= 0
      && conn.getTimeElapsedSinceLastUse() > poolPingConnectionsNotUsedFor) {
      try {
        if (log.isDebugEnabled()) {
          log.debug("Testing connection " + conn.getRealHashCode() + " ...");
        }
        // 获取真实的数据库连接
        Connection realConn = conn.getRealConnection();
        // 执行 poolPingQuery 字段中记录的测试 SQL 语句
        try (Statement statement = realConn.createStatement()) {
          statement.executeQuery(poolPingQuery).close();
        }
        // 如果连接未设置自动提交，则进行回滚操作
        if (!realConn.getAutoCommit()) {
          realConn.rollback();
        }
        // 不抛异常，即为成功
        result = true;
        if (log.isDebugEnabled()) {
          log.debug("Connection " + conn.getRealHashCode() + " is GOOD!");
        }
      } catch (Exception e) {
        log.warn("Execution of ping query '" + poolPingQuery + "' failed: " + e.getMessage());
        try {
          conn.getRealConnection().close();
        } catch (Exception e2) {
          // ignore
        }
        // 抛异常，即为失败
        result = false;
        if (log.isDebugEnabled()) {
          log.debug("Connection " + conn.getRealHashCode() + " is BAD: " + e.getMessage());
        }
      }
    }
    return result;
  }

  @Override
  protected void finalize() throws Throwable {
    forceCloseAll();
    super.finalize();
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new SQLException(getClass().getName() + " is not a wrapper.");
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) {
    return false;
  }

  @Override
  public Logger getParentLogger() {
    return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  }

}
