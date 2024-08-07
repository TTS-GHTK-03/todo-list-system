package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class LabelNotFoundException extends NotFoundException {
  public LabelNotFoundException() {
    setStatus(404);
    setCode("LabelNotFoundException");
  }
}
