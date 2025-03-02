package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class TaskNotExistUserException extends BadRequestException {
  public TaskNotExistUserException() {
    setStatus(400);
    setCode("TaskNotExistUserException");
  }
}
