package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class SprintStatusNotFoundException extends NotFoundException {
  public SprintStatusNotFoundException() {
    setStatus(404);
    setCode("SprintStatusNotFoundException");
  }
}
