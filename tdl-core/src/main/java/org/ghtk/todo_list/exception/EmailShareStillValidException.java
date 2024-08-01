package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class EmailShareStillValidException extends BadRequestException {

  public EmailShareStillValidException(String email) {
    setStatus(400);
    setCode("EmailShareStillValidException");
    addParams("email", email);
  }
}
