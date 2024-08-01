package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class RoleProjectUserNotFound extends NotFoundException {
  public RoleProjectUserNotFound(){
    setStatus(404);
    setCode("RoleProjectUserNotFound");
  }
}
