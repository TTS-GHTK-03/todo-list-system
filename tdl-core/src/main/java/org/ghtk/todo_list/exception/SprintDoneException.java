package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class SprintDoneException extends BadRequestException {
  public SprintDoneException() {
    setStatus(400);
    setCode("SprintDoneException");
  }
}
