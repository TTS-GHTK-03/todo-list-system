package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class TaskAssignmentExistsException extends BadRequestException {
  public TaskAssignmentExistsException(){
    setStatus(400);
    setCode("TaskAssignmentExistsException");
  }
}
