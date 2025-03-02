package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.ConflictException;

public class UsernameAlreadyExistedException extends ConflictException {

  public UsernameAlreadyExistedException(String username) {
    setStatus(409);
    setCode("UsernameAlreadyExistedException");
    addParams("username", username);
  }
}
