package com.fiuba.common.service;


import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnlineRejectedExecutionHandler implements RejectedExecutionHandler {
  private static final Logger logger = LoggerFactory
      .getLogger(OnlineRejectedExecutionHandler.class);

  @Override
  public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
    logger.info(r.toString() + " es rechazado");
  }

}
