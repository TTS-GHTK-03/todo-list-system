package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class TaskStatusNotFoundException extends NotFoundException {
  public TaskStatusNotFoundException() {
    setStatus(404);
    setCode("org.ghtk.todo_list.exception.TaskStatusNotFoundException");
  }
}
