package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class AccountNotFoundException extends NotFoundException {

  public AccountNotFoundException() {
    setStatus(404);
    setCode("AccountNotFoundException");
  }
}
