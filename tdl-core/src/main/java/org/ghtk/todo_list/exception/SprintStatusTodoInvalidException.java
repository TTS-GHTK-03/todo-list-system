package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class SprintStatusTodoInvalidException extends BadRequestException {
  public SprintStatusTodoInvalidException() {
    setStatus(400);
    setCode("SprintStatusTodoInvalidException");
  }
}
