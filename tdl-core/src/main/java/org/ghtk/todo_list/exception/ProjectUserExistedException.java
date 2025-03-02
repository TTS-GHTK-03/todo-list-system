package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class ProjectUserExistedException extends BadRequestException {

  public ProjectUserExistedException() {
    setStatus(400);
    setCode("ProjectUserExistedException");
  }
}
