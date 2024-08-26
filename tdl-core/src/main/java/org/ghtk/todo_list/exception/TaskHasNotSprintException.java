package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class TaskHasNotSprintException extends BadRequestException {

  public TaskHasNotSprintException() {
    setStatus(400);
    setCode("TaskHasNotSprintException");
  }
}
