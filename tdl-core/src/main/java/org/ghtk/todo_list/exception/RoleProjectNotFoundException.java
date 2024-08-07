package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class RoleProjectNotFoundException extends NotFoundException {
  public RoleProjectNotFoundException(String role){
    setStatus(404);
    setCode("RoleProjectNotFoundException");
    addParams("role", role);
  }
}
