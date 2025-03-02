package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.base_authrization.BaseAuthorization;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.CommentFacadeService;
import org.ghtk.todo_list.model.request.CreateCommentRequest;
import org.ghtk.todo_list.model.request.UpdateCommentRequest;
import org.ghtk.todo_list.model.response.CommentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/projects")
@RestController
@Slf4j
@Tag(name = "Comment", description = "Comment API")
@RequiredArgsConstructor
public class CommentController {

  private final CommentFacadeService commentFacadeService;
  private final BaseAuthorization baseAuthorization;

  @PostMapping("/{project_id}/tasks/{task_id}/comments")
  @Operation(description = "Create comment")
  public BaseResponse<CommentResponse> createComment(
      @Valid @RequestBody CreateCommentRequest commentRequest,
      @Parameter(name = "task_id", description = "Identification of task")
      @PathVariable("task_id") String taskId,
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId) {
    log.info("(CreateComment)taskId: {}", taskId);
    baseAuthorization.allRole(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        commentFacadeService.createComment(getUserId(), projectId, taskId,
            commentRequest.getText()));
  }

  @PutMapping("/{project_id}/tasks/{task_id}/comments/{comment_id}")
  @Operation(description = "Update comment")
  public BaseResponse<CommentResponse> updateComment(
      @Valid @RequestBody UpdateCommentRequest updateCommentRequest,
      @Parameter(name = "task_id", description = "Identification of task")
      @PathVariable("task_id") String taskId,
      @Parameter(name = "comment_id", description = "Identification of comment")
      @PathVariable("comment_id") String commentId,
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId) {
    log.info("(UpdateComment)taskId: {}, commentId: {}", taskId, commentId);
    baseAuthorization.allRole(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        commentFacadeService.updateComment(getUserId(), projectId, taskId, commentId,
            updateCommentRequest.getText()));
  }

  @PostMapping("/{project_id}/tasks/{task_id}/comments/{comment_id}/reply")
  @Operation(description = "Reply comment")
  public BaseResponse<CommentResponse> replyComment(
      @Valid @RequestBody CreateCommentRequest request,
      @Parameter(name = "task_id", description = "Identification of task")
      @PathVariable("task_id") String taskId,
      @Parameter(name = "comment_id", description = "Identification of comment")
      @PathVariable("comment_id") String commentId,
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId) {
    log.info("(replyComment)taskId: {}, commentId: {}", taskId, commentId);
    baseAuthorization.allRole(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        commentFacadeService.replyComment(getUserId(), projectId, taskId, commentId,
            request.getText()));
  }

  @GetMapping("/{project_id}/tasks/{task_id}/comments")
  @Operation(description = "Get all comments by task id")
  public BaseResponse<List<CommentResponse>> getAllCommentsByTaskId(
      @Parameter(name = "task_id", description = "Identification of task")
      @PathVariable("task_id") String taskId,
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId) {
    log.info("(getAllCommentsByTaskId)taskId: {}", taskId);
    baseAuthorization.allRole(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        commentFacadeService.getAllCommentsByTaskId(taskId));
  }

  @GetMapping("/{project_id}/tasks/{task_id}/comments/{comment_id}")
  @Operation(description = "Get comment by comment id")
  public BaseResponse<CommentResponse> getCommentByCommentId(
      @Parameter(name = "task_id", description = "Identification of task")
      @PathVariable("task_id") String taskId,
      @Parameter(name = "comment_id", description = "Identification of comment")
      @PathVariable("comment_id") String commentId,
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId) {
    log.info("(getCommentByCommentId)taskId: {}, commentId: {}", taskId, commentId);
    baseAuthorization.allRole(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        commentFacadeService.findById(taskId, commentId));
  }

  @GetMapping("/{project_id}/tasks/{task_id}/comments/parent/{parent_id}")
  @Operation(description = "Get all comments by parent id")
  public BaseResponse<List<CommentResponse>> getAllCommentsByParentId(
      @Parameter(name = "task_id", description = "Identification of task")
      @PathVariable("task_id") String taskId,
      @Parameter(name = "parent_id", description = "Identification of parent")
      @PathVariable("parent_id") String parentId,
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId) {
    log.info("(getAllCommentsByParentId)taskId: {}, parentId: {}", taskId, parentId);
    baseAuthorization.allRole(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        commentFacadeService.getAllCommentsByParentId(taskId, parentId));
  }

  @DeleteMapping("/{project_id}/tasks/{task_id}/comments/{comment_id}")
  @Operation(description = "Delete comment")
  public BaseResponse<String> deleteComment(
      @Parameter(name = "task_id", description = "Identification of task")
      @PathVariable("task_id") String taskId,
      @Parameter(name = "comment_id", description = "Identification of comment")
      @PathVariable("comment_id") String commentId,
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId) {
    log.info("(deleteComment)taskId: {}, commentId: {}", taskId, commentId);
    baseAuthorization.allRole(getUserId(), projectId);
    commentFacadeService.deleteComment(getUserId(), taskId, commentId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Delete comment successfully!!");
  }
}


