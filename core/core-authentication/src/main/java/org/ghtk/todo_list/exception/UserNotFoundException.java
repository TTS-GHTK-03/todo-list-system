package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

  public UserNotFoundException() {
    setStatus(404);
    setCode("UserNotFoundException");
  }
}
