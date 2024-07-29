package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class EmailStillValidException extends BadRequestException {

  public EmailStillValidException(String email) {
    setStatus(400);
    setCode("org.ghtk.todo_list.exception.EmailStillValidException");
    addParams("email", email);
  }
}
