package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class EmailInvalidWithToken extends BadRequestException {
  public EmailInvalidWithToken(String email) {
    setStatus(400);
    setCode("org.ghtk.todo_list.exception.EmailInvalidWithToken");
    addParams("email", email);
  }
}
