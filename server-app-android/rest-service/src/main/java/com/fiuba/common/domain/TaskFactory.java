package com.fiuba.common.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fiuba.common.controller.GetUserByNameTask;

@Component
public class TaskFactory {

  @Autowired
  private GetUserByNameTask getUserByNameTask;

  private Map<Task, Callable<?>> taskMap = new HashMap<Task, Callable<?>>();

  @PostConstruct
  private void loadConfiguration() {
    taskMap.put(Task.GET_USER_BY_NAME, getUserByNameTask);
  }

  public Callable<?> getTask(Task task) {
    if (taskMap.containsKey(task)) {
      return taskMap.get(task);
    }

    throw new IllegalStateException("La task= " + task + " no esta cargada correctamente");
  }

}
