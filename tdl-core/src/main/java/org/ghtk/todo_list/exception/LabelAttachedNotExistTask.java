package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class LabelAttachedNotExistTask extends BadRequestException {
  public LabelAttachedNotExistTask() {
    setStatus(400);
    setCode("LabelAttachedNotExistTask");
  }
}
