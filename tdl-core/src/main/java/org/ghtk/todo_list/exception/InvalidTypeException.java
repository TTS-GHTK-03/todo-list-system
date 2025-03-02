package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class InvalidTypeException extends BadRequestException {
  public InvalidTypeException(){
    setStatus(400);
    setCode("InvalidTypeException");
  }
}
