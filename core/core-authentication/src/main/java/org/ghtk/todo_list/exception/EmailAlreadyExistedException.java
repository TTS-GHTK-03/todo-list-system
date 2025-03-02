package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.ConflictException;

public class EmailAlreadyExistedException extends ConflictException {

  public EmailAlreadyExistedException(String email) {
    setStatus(409);
    setCode("EmailAlreadyExistedException");
    addParams("email", email);
  }
}
