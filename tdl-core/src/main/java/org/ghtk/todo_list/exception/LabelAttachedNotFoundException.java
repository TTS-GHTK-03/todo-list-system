package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class LabelAttachedNotFoundException extends NotFoundException {

  public LabelAttachedNotFoundException(){
    setStatus(404);
    setCode("LabelAttachedNotFoundException");
  }
}
