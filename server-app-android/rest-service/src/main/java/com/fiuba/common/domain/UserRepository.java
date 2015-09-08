package com.fiuba.common.domain;

import com.fiuba.common.domain.model.User;


public interface UserRepository {

  User getUser(String name);

}
