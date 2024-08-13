package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class ResetPasswordKeyNotFoundException extends NotFoundException {
  public ResetPasswordKeyNotFoundException() {
    setStatus(404);
    setCode("ResetPasswordKeyNotFoundException");
  }
}
