package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class LabelAttachedAlreadyExistsException extends BadRequestException {
  public LabelAttachedAlreadyExistsException() {
    setStatus(400);
    setCode("LabelAttachedAlreadyExistsException");
  }
}
