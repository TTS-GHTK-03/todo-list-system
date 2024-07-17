package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.Comment;
import org.ghtk.todo_list.model.response.CreateCommentResponse;

public interface CommentService {
  CreateCommentResponse createComment(String userId, String taskId, String text);
}
