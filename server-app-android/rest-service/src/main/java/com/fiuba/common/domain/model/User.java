package com.fiuba.common.domain.model;

import org.parse4j.ParseObject;


public class User {

  private String name;
  private String password;

  public User(String name, String password) {
    super();
    this.name = name;
    this.password = password;
  }

  public User(ParseObject parseObject) {
    this.name = parseObject.getString("name");
    this.password = parseObject.getString("password");
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }



}
