package org.ghtk.todo_list.core_exception.exception;

public class InternalErrorException extends BaseException {
  public InternalErrorException() {
    setStatus(500);
    setCode("InternalErrorException");
  }
}
