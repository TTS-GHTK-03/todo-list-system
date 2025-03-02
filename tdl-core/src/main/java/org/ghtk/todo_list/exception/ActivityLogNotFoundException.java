package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class ActivityLogNotFoundException extends NotFoundException {

  public ActivityLogNotFoundException() {
    setStatus(404);
    setCode("ActivityLogNotFoundException");
  }
}
