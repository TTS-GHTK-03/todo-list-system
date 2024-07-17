package org.ghtk.todo_list.service.impl;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Comment;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.model.response.CreateCommentResponse;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.ghtk.todo_list.repository.CommentRepository;
import org.ghtk.todo_list.repository.TaskRepository;
import org.ghtk.todo_list.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {

  private final CommentRepository commentRepository;

  @Override
  public CreateCommentResponse createComment(String userId, String taskId, String text) {
    log.info("(CreateComment)user: {}, taskId: {},", userId, taskId);
    Comment comment = new Comment();
    comment.setTaskId(taskId);
    comment.setUserId(userId);
    comment.setText(text);

    Comment savedComment = commentRepository.save(comment);
    return CreateCommentResponse.builder()
        .id(savedComment.getId())
        .text(savedComment.getText())
        .taskId(savedComment.getTaskId())
        .userId(savedComment.getUserId())
        .createdAt(savedComment.getCreatedAt())
        .lastUpdatedAt(savedComment.getLastUpdatedAt())
        .build();
  }
}
