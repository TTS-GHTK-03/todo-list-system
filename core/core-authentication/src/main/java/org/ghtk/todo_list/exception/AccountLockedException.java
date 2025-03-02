package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class AccountLockedException extends BadRequestException {

  public AccountLockedException() {
    setStatus(400);
    setCode("AccountLockedException");
  }
}
