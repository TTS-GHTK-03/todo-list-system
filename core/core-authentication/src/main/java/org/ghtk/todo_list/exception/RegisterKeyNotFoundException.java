package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class RegisterKeyNotFoundException extends NotFoundException {

  public RegisterKeyNotFoundException(String email) {
    setStatus(404);
    setCode("org.ghtk.todo_list.exception.RegisterKeyNotFoundException");
    addParams("email", email);
  }
}
