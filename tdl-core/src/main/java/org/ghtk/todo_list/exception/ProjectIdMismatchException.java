package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.ConflictException;

public class ProjectIdMismatchException extends ConflictException {
  public ProjectIdMismatchException() {
    setStatus(409);
    setCode("org.ghtk.todo_list.exception.ProjectIdMismatchException");
  }
}
