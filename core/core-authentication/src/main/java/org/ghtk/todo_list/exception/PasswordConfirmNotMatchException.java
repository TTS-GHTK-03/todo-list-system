package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class PasswordConfirmNotMatchException extends BadRequestException {

  public PasswordConfirmNotMatchException() {
    setStatus(400);
    setCode("PasswordConfirmNotMatchException");
  }
}
