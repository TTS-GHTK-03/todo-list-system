package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class TypeResendInvalidException extends BadRequestException {
  public TypeResendInvalidException(String type) {
    setStatus(400);
    setCode("TypeResendInvalidException");
    addParams("type", type);
  }
}
