package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.UnauthorizedException;

public class UserUnauthorizedException extends UnauthorizedException {

  public UserUnauthorizedException(){
    setStatus(401);
    setCode("UserUnauthorizedException");
  }
}
