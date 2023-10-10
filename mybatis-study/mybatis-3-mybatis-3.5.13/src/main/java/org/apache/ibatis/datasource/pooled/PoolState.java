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

import java.util.ArrayList;
import java.util.List;

/**
 * 连接池状态，用于记录连接池的状态信息
 *
 * @author Clinton Begin
 */
public class PoolState {

  /**
   * 空闲连接集合
   */
  protected final List<PooledConnection> idleConnections = new ArrayList<>();
  /**
   * 活跃连接集合
   */
  protected final List<PooledConnection> activeConnections = new ArrayList<>();
  /**
   * 池化数据源
   */
  protected PooledDataSource dataSource;
  /**
   * 请求数据库连接的次数
   */
  protected long requestCount;
  /**
   * 获取连接的累计耗时（单位：毫秒）
   */
  protected long accumulatedRequestTime;
  /**
   * 所有连接被使用的累计耗时（单位：毫秒）
   */
  protected long accumulatedCheckoutTime;
  /**
   * 执行时间超时的连接数
   */
  protected long claimedOverdueConnectionCount;
  /**
   * 超时时间累加值（单位：毫秒）
   */
  protected long accumulatedCheckoutTimeOfOverdueConnections;
  /**
   * 阻塞等待的累计时间（单位：毫秒）
   */
  protected long accumulatedWaitTime;
  /**
   * 阻塞等待的总次数
   */
  protected long hadToWaitCount;
  /**
   * 无效连接数
   */
  protected long badConnectionCount;

  public PoolState(PooledDataSource dataSource) {
    this.dataSource = dataSource;
  }

  public synchronized long getRequestCount() {
    return requestCount;
  }

  public synchronized long getAverageRequestTime() {
    return requestCount == 0 ? 0 : accumulatedRequestTime / requestCount;
  }

  public synchronized long getAverageWaitTime() {
    return hadToWaitCount == 0 ? 0 : accumulatedWaitTime / hadToWaitCount;

  }

  public synchronized long getHadToWaitCount() {
    return hadToWaitCount;
  }

  public synchronized long getBadConnectionCount() {
    return badConnectionCount;
  }

  public synchronized long getClaimedOverdueConnectionCount() {
    return claimedOverdueConnectionCount;
  }

  public synchronized long getAverageOverdueCheckoutTime() {
    return claimedOverdueConnectionCount == 0 ? 0
      : accumulatedCheckoutTimeOfOverdueConnections / claimedOverdueConnectionCount;
  }

  public synchronized long getAverageCheckoutTime() {
    return requestCount == 0 ? 0 : accumulatedCheckoutTime / requestCount;
  }

  public synchronized int getIdleConnectionCount() {
    return idleConnections.size();
  }

  public synchronized int getActiveConnectionCount() {
    return activeConnections.size();
  }

  @Override
  public synchronized String toString() {
    String builder = "\n===CONFIGURATION==============================================" +
      "\n jdbcDriver                     " + dataSource.getDriver() +
      "\n jdbcUrl                        " + dataSource.getUrl() +
      "\n jdbcUsername                   " + dataSource.getUsername() +
      "\n jdbcPassword                   " +
      (dataSource.getPassword() == null ? "NULL" : "************") +
      "\n poolMaxActiveConnections       " + dataSource.poolMaximumActiveConnections +
      "\n poolMaxIdleConnections         " + dataSource.poolMaximumIdleConnections +
      "\n poolMaxCheckoutTime            " + dataSource.poolMaximumCheckoutTime +
      "\n poolTimeToWait                 " + dataSource.poolTimeToWait +
      "\n poolPingEnabled                " + dataSource.poolPingEnabled +
      "\n poolPingQuery                  " + dataSource.poolPingQuery +
      "\n poolPingConnectionsNotUsedFor  " + dataSource.poolPingConnectionsNotUsedFor +
      "\n ---STATUS-----------------------------------------------------" +
      "\n activeConnections              " + getActiveConnectionCount() +
      "\n idleConnections                " + getIdleConnectionCount() +
      "\n requestCount                   " + getRequestCount() +
      "\n averageRequestTime             " + getAverageRequestTime() +
      "\n averageCheckoutTime            " + getAverageCheckoutTime() +
      "\n claimedOverdue                 " + getClaimedOverdueConnectionCount() +
      "\n averageOverdueCheckoutTime     " + getAverageOverdueCheckoutTime() +
      "\n hadToWait                      " + getHadToWaitCount() +
      "\n averageWaitTime                " + getAverageWaitTime() +
      "\n badConnectionCount             " + getBadConnectionCount() +
      "\n===============================================================";
    return builder;
  }

}
