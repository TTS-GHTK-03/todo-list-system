package org.ghtk.todo_list.facade.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Comment;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.exception.UserInvalidException;
import org.ghtk.todo_list.exception.UserNotFoundException;
import org.ghtk.todo_list.facade.CommentFacadeService;
import org.ghtk.todo_list.model.response.CommentResponse;
import org.ghtk.todo_list.service.CommentService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.TaskService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentFacadeServiceImp implements CommentFacadeService {

  private final TaskService taskService;
  private final CommentService commentService;
  private final ProjectService projectService;

  @Override
  public CommentResponse createComment(String userId, String projectId, String taskid, String text) {
    log.info("(createComment)userId: {},taskId: {}", userId, taskid);
    validateProjectId(projectId);
    validateTaskId(taskid);
    return commentService.createComment(userId, taskid, text);
  }

  @Override
  public CommentResponse updateComment(String userId, String projectId, String taskId, String commentId,
      String text) {
    log.info("(updateComment)userId: {},taskId: {}", userId, taskId);
    validateProjectId(projectId);
    validateTaskId(taskId);
    Comment comment = commentService.findById(commentId);
    if (!userId.equals(comment.getUserId())) {
      log.error("(updateComment)userId: {}, commentId: {}", userId, commentId);
      throw new UserInvalidException();
    }
    comment.setText(text);
    Comment savedComment = commentService.save(comment);
    return CommentResponse.builder()
        .id(savedComment.getId())
        .text(savedComment.getText())
        .parentId(savedComment.getParentId())
        .taskId(savedComment.getTaskId())
        .userId(savedComment.getUserId())
        .createdAt(savedComment.getCreatedAt())
        .lastUpdatedAt(savedComment.getLastUpdatedAt())
        .build();
  }

  @Override
  public CommentResponse replyComment(String userId, String projectId, String taskId, String commentId, String text) {
    log.info("(replyComment)userId: {},taskId: {}, commentId: {}, text: {}", userId, taskId, commentId, text);
    validateProjectId(projectId);
    validateTaskId(taskId);
    return commentService.replyComment(userId, taskId, commentId, text);
  }

  void validateTaskId(String taskId) {
    log.info("(validateTaskId)taskId: {}", taskId);
    if (!taskService.existById(taskId)) {
      log.error("(validateTaskId)taskId: {}", taskId);
      throw new TaskNotFoundException();
    }
  }

  void validateProjectId(String projectId) {
    log.info("(validateProjectId)projectId: {}", projectId);
    if (!projectService.existById(projectId)) {
      log.error("(validateProjectId)projectId: {}", projectId);
      throw new ProjectNotFoundException();
    }
  }
}
