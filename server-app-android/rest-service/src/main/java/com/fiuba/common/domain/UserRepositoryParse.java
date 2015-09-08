package com.fiuba.common.domain;

import java.util.List;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.springframework.stereotype.Repository;

import com.fiuba.common.domain.model.User;

@Repository
public class UserRepositoryParse implements UserRepository {

  @Override
  public User getUser(String name) {

    try {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Usuario");
      query.whereEqualTo("Nombre", name);
      List<ParseObject> result = query.find();
      if (!result.isEmpty()) {
        ParseObject parseObject = result.iterator().next();
        return new User(parseObject);
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;

  }


}
