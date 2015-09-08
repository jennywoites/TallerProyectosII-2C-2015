package com.fiuba.common.controller;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fiuba.common.domain.UserRepository;
import com.fiuba.common.domain.model.User;

@Component
public class GetUserByNameTask implements Callable<User> {

  private UserRepository repository;
  private String name;

  @Autowired
  public GetUserByNameTask(UserRepository repository) {
    this.repository = repository;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public User call() throws Exception {
    if (name != null) {
      return repository.getUser(name);
    }

    throw new IllegalStateException("Se debe cargar un nombre");

  }

}
