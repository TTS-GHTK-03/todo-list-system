package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class TypeTitleAlreadyExistedException extends BadRequestException {
  public TypeTitleAlreadyExistedException(){
    setStatus(400);
    setCode("TypeTitleAlreadyExistedException");
  }
}
