package org.ghtk.todo_list.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Comment;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.repository.CommentRepository;
import org.ghtk.todo_list.repository.TaskRepository;
import org.ghtk.todo_list.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommentServiceImp implements CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Override
  public Comment createComment(String userId, String taskId, String text) {
    Comment comment = new Comment();
    comment.setTaskId(taskId);
    comment.setUserId(userId);
    comment.setText(text);

    Comment savedComment = commentRepository.save(comment);
    return savedComment;
  }
}
