package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.entity.Comment;
import org.ghtk.todo_list.model.response.CommentResponse;

public interface CommentFacadeService {

  CommentResponse createComment(String userId, String projectId, String taskid, String text);

  CommentResponse updateComment(String userId, String projectId, String taskId, String commentId,
      String text);

  CommentResponse replyComment(String userId, String projectId, String taskId, String commentId,
      String text);

  List<CommentResponse> getAllCommentsByTaskId(String taskId);

  List<CommentResponse> getAllCommentsByParentId(String taskId, String parentId);

  CommentResponse findById(String taskId, String commentId);

  void deleteComment(String userId, String taskId, String commentId);
}
