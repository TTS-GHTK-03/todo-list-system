package org.ghtk.todo_list.core_exception.exception;

public class ForbiddenException extends BaseException {

  public ForbiddenException() {
    setStatus(403);
    setCode("ForbiddenException");
  }
}
