package org.ghtk.todo_list.facade.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Comment;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.exception.UserNotFoundException;
import org.ghtk.todo_list.facade.CommentFacadeService;
import org.ghtk.todo_list.model.response.CommentResponse;
import org.ghtk.todo_list.service.CommentService;
import org.ghtk.todo_list.service.TaskService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentFacadeServiceImp implements CommentFacadeService {

  private final TaskService taskService;
  private final CommentService commentService;

  @Override
  public CommentResponse createComment(String userId, String taskid, String text) {
    log.info("(createComment)userId: {},taskId: {}", userId, taskid);
    validateTaskId(taskid);
    return commentService.createComment(userId, taskid, text);
  }

  @Override
  public CommentResponse updateComment(String userId, String taskId, String commentId,
      String text) {
    log.info("(updateComment)userId: {},taskId: {}", userId, taskId);
    validateTaskId(taskId);
    validateUserId(userId,commentId);
    return commentService.updateComment(userId, taskId, commentId, text);
  }

  void validateTaskId(String taskId) {
    log.info("(validateTaskId)taskId: {}", taskId);
    if (!taskService.existById(taskId)) {
      log.error("(validateTaskId)taskId: {}", taskId);
      throw new TaskNotFoundException();
    }
  }

  void validateUserId(String userId, String commentId) {
    log.info("(validateUserId)userId: {}, commentId: {}", userId, commentId);
    Comment comment = commentService.findById(commentId);
    if (!userId.equals(comment.getUserId())) {
      log.error("(validateUserId)userId: {}, commentId: {}", userId, commentId);
      throw new UserNotFoundException();
    }
  }
}
