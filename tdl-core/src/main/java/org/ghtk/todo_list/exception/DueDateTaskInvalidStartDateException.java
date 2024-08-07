package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class DueDateTaskInvalidStartDateException extends BadRequestException {
  public DueDateTaskInvalidStartDateException(){
    setStatus(400);
    setCode("DueDateTaskInvalidStartDateException");
  }
}
