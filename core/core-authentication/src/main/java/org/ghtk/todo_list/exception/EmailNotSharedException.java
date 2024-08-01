package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;
public class EmailNotSharedException extends BadRequestException {
  public EmailNotSharedException() {
    setStatus(400);
    setCode("EmailNotSharedException");
  }
}
