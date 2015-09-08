package com.fiuba.common.domain;

import java.util.Properties;

public class ThreadPoolConfiguration {
  private static final String THREAD_POOL_CORE_SIZE = "threadPool.coresSize";
  private static final String THREAD_POOL_MAXIMUM_POOL_SIZE = "threadPool.maximumPoolSize";
  private static final String THREAD_POOL_KEEP_ALIVE_TIME = "threadPool.keepAliveTime";


  private Properties properties;

  public ThreadPoolConfiguration(Properties properties) {
    this.properties = properties;
  }

  public int getThreadPoolCoreSize() {
    return Integer.parseInt(properties.getProperty(THREAD_POOL_CORE_SIZE));
  }

  public int getThreadPoolMaximumPoolSize() {
    return Integer.parseInt(properties.getProperty(THREAD_POOL_MAXIMUM_POOL_SIZE));
  }

  public long getThreadPoolKeepAliveTime() {
    return Long.parseLong(properties.getProperty(THREAD_POOL_KEEP_ALIVE_TIME));
  }

}
