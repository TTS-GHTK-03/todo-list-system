package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class UsernameNotFoundException extends NotFoundException {

  public UsernameNotFoundException(String username) {
    setStatus(404);
    setCode("org.ghtk.todo_list.exception.UsernameNotFoundException");
    addParams("username", username);
  }
}
