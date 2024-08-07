package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class RoleSharedUserExceededException extends BadRequestException {
  public RoleSharedUserExceededException() {
    setStatus(400);
    setCode("RoleSharedUserExceededException");
  }
}
