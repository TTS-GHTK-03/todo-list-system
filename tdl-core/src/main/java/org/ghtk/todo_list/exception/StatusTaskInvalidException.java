package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class StatusTaskInvalidException extends BadRequestException {

  public StatusTaskInvalidException(){
    setStatus(400);
    setCode("StatusTaskInvalidException");
  }
}
