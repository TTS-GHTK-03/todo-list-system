package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class TypeNotAllowDeleteException extends BadRequestException {
  public TypeNotAllowDeleteException() {
    setStatus(400);
    setCode("TypeNotAllowDeleteException");
  }
}
