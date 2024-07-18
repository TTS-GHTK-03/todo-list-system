package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.CommentFacadeService;
import org.ghtk.todo_list.model.request.CreateCommentRequest;
import org.ghtk.todo_list.model.request.UpdateCommentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/projects")
@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

  private final CommentFacadeService commentFacadeService;

  @PostMapping("/tasks/{task_id}/comments")
  public BaseResponse createComment(
      @Valid @RequestBody CreateCommentRequest commentRequest,
      @PathVariable("task_id") String taskId) {
    log.info("(CreateComment)taskId: {}", taskId);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        commentFacadeService.createComment(getUserId(), taskId, commentRequest.getText()));
  }

  @PostMapping("/tasks/{task_id}/comments/{comment_id}")
  public BaseResponse updateComment(
      @Valid @RequestBody UpdateCommentRequest updateCommentRequest,
      @PathVariable("task_id") String taskId, @PathVariable("comment_id") String commentId) {
    log.info("(UpdateComment)taskId: {}, commentId: {}", taskId, commentId);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        commentFacadeService.updateComment(getUserId(), taskId, commentId,
            updateCommentRequest.getText()));
  }

}

