package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
  public CommentNotFoundException() {
    setStatus(404);
    setCode("CommentNotFoundException");
  }
}
