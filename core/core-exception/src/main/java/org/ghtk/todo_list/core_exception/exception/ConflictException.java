package org.ghtk.todo_list.core_exception.exception;

public class ConflictException extends BaseException {
  public ConflictException() {
    setStatus(409);
    setCode("ConflictException");
  }
}
