package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class PasswordDatabaseNotMatchException extends BadRequestException {
  public PasswordDatabaseNotMatchException() {
    setStatus(400);
    setCode("org.ghtk.todo_list.exception.PasswordDatabaseNotMatchException");
  }
}
