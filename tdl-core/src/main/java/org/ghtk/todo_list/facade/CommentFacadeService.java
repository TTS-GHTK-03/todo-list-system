package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.entity.Comment;
import org.ghtk.todo_list.model.response.CommentResponse;

public interface CommentFacadeService {

  CommentResponse createComment(String userId, String taskid, String text);

  CommentResponse updateComment(String userId, String taskId, String commentId, String text);

}
