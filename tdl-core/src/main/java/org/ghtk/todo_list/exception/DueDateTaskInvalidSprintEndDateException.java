package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class DueDateTaskInvalidSprintEndDateException extends BadRequestException {
  public DueDateTaskInvalidSprintEndDateException(){
    setStatus(400);
    setCode("DueDateTaskInvalidSprintEndDateException");
  }
}
