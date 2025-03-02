package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class TypeNotFoundException extends NotFoundException {
  public TypeNotFoundException() {
    setStatus(404);
    setCode("TypeNotFoundException");
  }
}
