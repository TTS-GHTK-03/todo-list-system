package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class SprintNotStartException extends BadRequestException {
  public SprintNotStartException() {
    setStatus(400);
    setCode("SprintNotStartException");
  }
}
