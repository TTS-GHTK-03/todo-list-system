package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class EmailNotInviteExcpetion extends BadRequestException {
  public EmailNotInviteExcpetion(){
    setStatus(400);
    setCode("org.ghtk.todo_list.exception.EmailNotInviteExcpetion");
  }
}
