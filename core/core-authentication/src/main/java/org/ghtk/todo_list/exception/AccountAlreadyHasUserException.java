package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.ConflictException;

public class AccountAlreadyHasUserException extends ConflictException {

  public AccountAlreadyHasUserException() {
    setStatus(409);
    setCode("AccountAlreadyHasUserException");
  }
}
