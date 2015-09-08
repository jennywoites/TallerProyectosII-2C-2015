package com.fiuba.common.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fiuba.common.domain.Configuration;
import com.fiuba.common.domain.Task;
import com.fiuba.common.domain.TaskFactory;
import com.fiuba.common.domain.model.User;
import com.fiuba.common.service.OnlineThreadPoolExecutor;

@RestController
public class UserController {

  private OnlineThreadPoolExecutor onlineThreadPoolExecutor;
  private TaskFactory taskFactory;

  @Autowired
  private UserController(OnlineThreadPoolExecutor onlineThreadPoolExecutor, TaskFactory taskFactory) {
    this.onlineThreadPoolExecutor = onlineThreadPoolExecutor;
    this.taskFactory = taskFactory;
  }

  @RequestMapping("/getUser")
  public User getUser(@RequestParam(value = "name") String name) throws InterruptedException,
      ExecutionException, TimeoutException {
    GetUserByNameTask getUserTask = (GetUserByNameTask) taskFactory.getTask(Task.GET_USER_BY_NAME);
    getUserTask.setName(name);
    Future<User> result = onlineThreadPoolExecutor.submit(getUserTask);
    User user = result.get(Configuration.TIME_OUT_SECONDS, TimeUnit.SECONDS);
    return user;
  }
}
