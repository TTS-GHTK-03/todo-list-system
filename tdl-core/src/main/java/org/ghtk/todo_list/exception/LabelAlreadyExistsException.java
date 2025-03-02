package org.ghtk.todo_list.exception;


import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class LabelAlreadyExistsException extends BadRequestException {
  public LabelAlreadyExistsException() {
    setStatus(400);
    setCode("LabelAlreadyExistsException");
  }
}
