package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class OtpNotFoundException extends NotFoundException {

  public OtpNotFoundException() {
    setStatus(404);
    setCode("org.ghtk.todo_list.exception.OtpNotFoundException");
  }
}
