package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class StatusTaskKeyNotFoundException extends NotFoundException {
  public StatusTaskKeyNotFoundException() {
    setStatus(404);
    setCode("StatusTaskKeyNotFoundException");
  }
}
