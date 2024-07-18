package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.entity.Comment;
import org.ghtk.todo_list.model.response.CommentResponse;

public interface CommentFacadeService {

  CommentResponse createComment(String userId, String projectId, String taskid, String text);

  CommentResponse updateComment(String userId, String projectId, String taskId, String commentId, String text);

  CommentResponse replyComment(String userId, String projectId, String taskId, String commentId, String text);

}
