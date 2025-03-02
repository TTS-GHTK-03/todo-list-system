package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class ProjectIdMismatchException extends BadRequestException {
  public ProjectIdMismatchException() {
    setStatus(400);
    setCode("ProjectIdMismatchException");
  }
}
