package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class PasswordSimilarException extends BadRequestException {
  public PasswordSimilarException() {
    setStatus(400);
    setCode("PasswordSimilarException");
  }
}
