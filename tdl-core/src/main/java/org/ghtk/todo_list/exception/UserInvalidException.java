package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class UserInvalidException extends BadRequestException {

  public UserInvalidException(){
    setStatus(400);
    setCode("UserInvalidException");
  }
}
