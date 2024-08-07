package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class PasswordIncorrectException extends BadRequestException {

  public PasswordIncorrectException() {
    setStatus(400);
    setCode("PasswordIncorrectException");
  }
}

