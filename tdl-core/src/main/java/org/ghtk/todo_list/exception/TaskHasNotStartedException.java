package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class TaskHasNotStartedException extends BadRequestException {
  public TaskHasNotStartedException() {
    setStatus(400);
    setCode("TaskHasNotStartedException");
  }
}
