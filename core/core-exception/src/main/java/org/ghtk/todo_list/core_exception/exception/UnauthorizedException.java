package org.ghtk.todo_list.core_exception.exception;

public class UnauthorizedException extends BaseException {
  public UnauthorizedException() {
    setStatus(401);
    setCode("UnauthorizedException");
  }
}
