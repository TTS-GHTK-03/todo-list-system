package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class TypeNotExistProjectException extends BadRequestException {
  public TypeNotExistProjectException() {
    setStatus(400);
    setCode("TypeNotExistProjectException");
  }
}
