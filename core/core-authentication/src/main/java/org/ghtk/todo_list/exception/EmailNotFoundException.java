package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class EmailNotFoundException extends NotFoundException {

  public EmailNotFoundException(String email) {
    setStatus(404);
    setCode("org.ghtk.todo_list.exception.EmailNotFoundException");
    addParams("email", email);
  }
}
