package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class SprintNotExistProjectException extends BadRequestException {
  public SprintNotExistProjectException() {
    setStatus(400);
    setCode("SprintNotExistProjectException");
  }
}
