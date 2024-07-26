package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Tag(name = "comment")
@RequiredArgsConstructor
public class CommentController {

  private final CommentFacadeService commentFacadeService;

  @PostMapping("/{project_id}/tasks/{task_id}/comments")
  @Operation(description = "Tạo comment")
  public BaseResponse<CommentResponse> createComment(
      @Valid @RequestBody CreateCommentRequest commentRequest,
      @PathVariable("task_id") String taskId, @PathVariable("project_id") String projectId) {
    log.info("(CreateComment)taskId: {}", taskId);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        commentFacadeService.createComment(getUserId(), projectId, taskId,
            commentRequest.getText()));
  }

  @PutMapping("/{project_id}/tasks/{task_id}/comments/{comment_id}")
  public BaseResponse updateComment(
      @Valid @RequestBody UpdateCommentRequest updateCommentRequest,
      @Parameter(name = "task_id", description = "Đầu vào comment")
      @PathVariable("task_id") String taskId,
      @Parameter(name = "comment_id", description = "Đầu vào comment")
      @PathVariable("comment_id") String commentId,
      @Parameter(name = "project_id", description = "Đầu vào comment")
      @PathVariable("project_id") String projectId) {
    log.info("(UpdateComment)taskId: {}, commentId: {}", taskId, commentId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        commentFacadeService.updateComment(getUserId(), projectId, taskId, commentId,
            updateCommentRequest.getText()));
  }

  @PostMapping("/{project_id}/tasks/{task_id}/comments/{comment_id}/reply")
  public BaseResponse replyComment(
      @Valid @RequestBody CreateCommentRequest request,
      @Parameter(name = "task_id", description = "Đầu vào comment")
      @PathVariable("task_id") String taskId,
      @Parameter(name = "comment_id", description = "Đầu không comment")
      @PathVariable("comment_id") String commentId, @PathVariable("project_id") String projectId) {
    log.info("(replyComment)taskId: {}, commentId: {}", taskId, commentId);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        commentFacadeService.replyComment(getUserId(), projectId, taskId, commentId,
            request.getText()));
  }

  @GetMapping("/{task_id}/comments")
  public BaseResponse getAllCommentsByTaskId(@PathVariable("task_id") String taskId) {
    log.info("(getAllCommentsByTaskId)taskId: {}", taskId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        commentFacadeService.getAllCommentsByTaskId(taskId));
  }

  @GetMapping("/{task_id}/comments/{comment_id}")
  public BaseResponse getCommentByCommentId(@PathVariable("task_id") String taskId,
      @PathVariable("comment_id") String commentId) {
    log.info("(getCommentByCommentId)taskId: {}, commentId: {}", taskId, commentId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        commentFacadeService.findById(taskId, commentId));
  }

  @GetMapping("/{task_id}/comments/parent/{parent_id}")
  public BaseResponse getAllCommentsByParentId(@PathVariable("task_id") String taskId,
      @PathVariable("parent_id") String parentId) {
    log.info("(getAllCommentsByParentId)taskId: {}, parentId: {}", taskId, parentId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        commentFacadeService.getAllCommentsByParentId(taskId, parentId));
  }

  @DeleteMapping("/{task_id}/comments/{comment_id}")
  public BaseResponse deleteComment(@PathVariable("task_id") String taskId,
      @PathVariable("comment_id") String commentId) {
    log.info("(deleteComment)taskId: {}, commentId: {}", taskId, commentId);
    commentFacadeService.deleteComment(getUserId(), taskId, commentId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Delete comment successfully!!");
  }
}


