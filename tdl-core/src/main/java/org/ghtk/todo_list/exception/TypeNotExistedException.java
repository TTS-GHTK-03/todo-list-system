package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class TypeNotExistedException extends BadRequestException {

  public TypeNotExistedException() {
    setStatus(400);
    setCode("TypeNotExistedException");
  }
}
