package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class OTPNotFoundException extends NotFoundException {

  public OTPNotFoundException(String email) {
    setStatus(404);
    setCode("OTPNotFoundException");
    addParams("email", email);
  }
}
