package org.ghtk.todo_list.core_exception.exception;

public class BadRequestException extends BaseException {
  public BadRequestException() {
    setStatus(400);
    setCode("BadRequestException");
  }
}
