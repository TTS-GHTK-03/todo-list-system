package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class ActivityLogNotExistedException extends BadRequestException {

  public ActivityLogNotExistedException() {
    setStatus(400);
    setCode("ActivityLogNotExistedException");
  }
}
