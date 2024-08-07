package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class SprintNotFoundException extends NotFoundException {
  public SprintNotFoundException() {
    setStatus(404);
    setCode("SprintNotFoundException");
  }
}
