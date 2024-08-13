package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class OTPInvalidException extends BadRequestException {

  public OTPInvalidException(String otp) {
    setStatus(400);
    setCode("OTPInvalidException");
    addParams("otp", otp);
  }
}
