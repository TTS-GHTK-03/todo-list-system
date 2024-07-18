package org.ghtk.todo_list.service;

import java.util.List;
import org.ghtk.todo_list.entity.Comment;
import org.ghtk.todo_list.model.response.CommentResponse;

public interface CommentService {
  CommentResponse createComment(String userId, String taskId, String text);

  CommentResponse updateComment(String userId, String taskId, String commentId, String text);
  Comment findById(String commentId);
  List<CommentResponse> getAllCommentsByTaskId(String taskId);
  Comment save(Comment comment);
}
