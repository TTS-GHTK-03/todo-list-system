package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class LabelNotExistsException extends BadRequestException {

  public LabelNotExistsException() {
    setStatus(400);
    setCode("LabelNotExistsException");
  }
}
