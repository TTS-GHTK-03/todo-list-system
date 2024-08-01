package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class ProjectKeyAlreadyExistedException extends BadRequestException {
  public ProjectKeyAlreadyExistedException(){
    setStatus(400);
    setCode("ProjectKeyAlreadyExistedException");
  }
}
