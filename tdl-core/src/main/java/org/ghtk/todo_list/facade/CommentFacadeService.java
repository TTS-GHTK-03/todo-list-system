package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.entity.Comment;
import org.ghtk.todo_list.model.response.CreateCommentResponse;

public interface CommentFacadeService {

  CreateCommentResponse createComment(String userId, String taskid, String text);
}
