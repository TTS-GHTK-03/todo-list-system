package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.Comment;

public interface CommentService {
  Comment createComment(String userId, String taskId, String text);
}
