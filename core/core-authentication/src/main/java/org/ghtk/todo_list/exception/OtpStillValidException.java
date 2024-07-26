package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class OtpStillValidException extends BadRequestException {

  public OtpStillValidException(String email) {
    setStatus(400);
    setCode("org.ghtk.todo_list.exception.OtpStillValidException");
    addParams("email", email);
  }
}
