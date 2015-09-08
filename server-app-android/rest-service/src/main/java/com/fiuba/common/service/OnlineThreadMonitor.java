package com.fiuba.common.service;


import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnlineThreadMonitor implements Runnable {
  private Logger logger = LoggerFactory.getLogger(OnlineThreadMonitor.class);
  private ThreadPoolExecutor threadExecutor;
  private int delay;
  private boolean run = true;

  public OnlineThreadMonitor(ThreadPoolExecutor threadExecutor, int delay) {
    this.threadExecutor = threadExecutor;
    this.delay = delay;
  }

  public void shutdown() {
    this.run = false;
  }

  @Override
  public void run() {
    while (run) {
      logger
          .debug(String
              .format(
                  "[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                  this.threadExecutor.getPoolSize(), this.threadExecutor.getCorePoolSize(),
                  this.threadExecutor.getActiveCount(),
                  this.threadExecutor.getCompletedTaskCount(), this.threadExecutor.getTaskCount(),
                  this.threadExecutor.isShutdown(), this.threadExecutor.isTerminated()));
      try {
        TimeUnit.SECONDS.sleep(delay);
      } catch (InterruptedException e) {
      }
    }
  }
}
