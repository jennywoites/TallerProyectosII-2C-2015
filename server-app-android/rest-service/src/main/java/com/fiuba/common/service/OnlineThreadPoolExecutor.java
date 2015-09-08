package com.fiuba.common.service;



import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import com.fiuba.common.domain.ThreadPoolConfiguration;

public class OnlineThreadPoolExecutor extends ThreadPoolExecutor {
  private OnlineThreadMonitor monitor;
  private Thread monitorThread;

  public OnlineThreadPoolExecutor(ThreadPoolConfiguration configuration) {
    super(configuration.getThreadPoolCoreSize(), configuration.getThreadPoolMaximumPoolSize(),
        configuration.getThreadPoolKeepAliveTime(), TimeUnit.SECONDS,
        new LinkedBlockingQueue<Runnable>(), new OnlineRejectedExecutionHandler());
    monitor = new OnlineThreadMonitor(this, 3);
    monitorThread = new Thread(monitor);
    monitorThread.start();
  }

  @PreDestroy
  public void shutdownAndAwaitTermination() {
    monitorThread.interrupt();
    shutdown();
    try {
      if (!awaitTermination(60, TimeUnit.SECONDS)) {
        shutdownNow();

        if (!awaitTermination(60, TimeUnit.SECONDS)) {
          throw new RuntimeException("El executor nunca termino!");
        }
      }
    } catch (InterruptedException ie) {
      shutdownNow();
      Thread.currentThread().interrupt();
    }
  }

  public OnlineThreadMonitor getMonitor() {
    return monitor;
  }

}
