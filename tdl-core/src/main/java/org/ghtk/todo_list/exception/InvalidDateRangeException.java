package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class InvalidDateRangeException extends BadRequestException {
  public InvalidDateRangeException(){
    setStatus(400);
    setCode("InvalidDateRangeException");
  }
}
