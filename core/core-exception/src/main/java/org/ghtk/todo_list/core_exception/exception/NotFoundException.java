package org.ghtk.todo_list.core_exception.exception;

public class NotFoundException extends BaseException {
  public NotFoundException() {
    setStatus(404);
    setCode("NotFoundException");
  }
}
