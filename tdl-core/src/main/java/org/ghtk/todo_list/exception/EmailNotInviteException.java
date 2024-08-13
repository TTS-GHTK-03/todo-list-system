package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class EmailNotInviteException extends BadRequestException {
  public EmailNotInviteException(){
    setStatus(400);
    setCode("EmailNotInviteException");
  }
}
