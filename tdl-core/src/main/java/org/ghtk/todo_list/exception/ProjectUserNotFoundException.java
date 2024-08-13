package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class ProjectUserNotFoundException extends NotFoundException {
  public ProjectUserNotFoundException(){
    setStatus(404);
    setCode("ProjectUserNotFoundException");
  }
}
