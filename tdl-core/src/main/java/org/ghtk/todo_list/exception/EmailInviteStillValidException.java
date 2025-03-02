package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class EmailInviteStillValidException extends BadRequestException {
  public EmailInviteStillValidException(String email) {
    setStatus(400);
    setCode("EmailInviteStillValidException");
    addParams("email", email);
  }
}
