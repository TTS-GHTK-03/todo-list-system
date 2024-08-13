package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class RoleProjectNotAllowException extends BadRequestException {
  public RoleProjectNotAllowException() {
    setStatus(400);
    setCode("RoleProjectNotAllowException");
  }
}
