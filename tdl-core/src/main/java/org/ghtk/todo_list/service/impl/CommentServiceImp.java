package org.ghtk.todo_list.service.impl;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Comment;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.exception.CommentNotFoundException;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.model.response.CommentResponse;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.ghtk.todo_list.model.response.TaskResponse;
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
  public CommentResponse createComment(String userId, String taskId, String text) {
    log.info("(CreateComment)user: {}, taskId: {},", userId, taskId);
    Comment comment = new Comment();
    comment.setTaskId(taskId);
    comment.setUserId(userId);
    comment.setText(text);

    Comment savedComment = commentRepository.save(comment);
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
  public CommentResponse updateComment(String userId, String taskId, String commentId,
      String text) {
    log.info("(updateComment)userId: {}, taskId: {}, commentId: {}", userId, taskId, commentId);
    var comment = commentRepository
        .findById(commentId)
        .orElseThrow(() -> {
          log.error("(updateComment)userId: {}, taskId: {}, commentId: {}", userId, taskId,
              commentId);
          throw new CommentNotFoundException();
        });
    comment.setText(text);
    Comment savedComment = commentRepository.save(comment);
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
  public Comment findById(String commentId) {
    log.info("(findById)commentId: {}", commentId);
    return commentRepository.findById(commentId)
        .orElseThrow(() -> {
          log.error("(findById)commentId: {}", commentId);
          throw new CommentNotFoundException();
        });
  }

  @Override
  public List<CommentResponse> getAllCommentsByParentId(String taskId, String parentId) {
    log.info("(findAllByParentId)taskId: {},parentId: {}", taskId, parentId);
    List<Comment> comments = commentRepository.findAllByParentId(parentId);
    return comments.stream()
        .map(comment -> new CommentResponse(comment.getId(), comment.getText(),
            comment.getParentId(), comment.getTaskId(), comment.getUserId(), comment.getCreatedAt(),
            comment.getLastUpdatedAt())).collect(Collectors.toList());
  }

  @Override
  public List<CommentResponse> getAllCommentsByTaskId(String taskId) {
    log.info("(findAllByTaskId)taskId: {}", taskId);
    List<Comment> comments = commentRepository.findAllByTaskId(taskId);
    return comments.stream()
        .map(comment -> new CommentResponse(comment.getId(), comment.getText(),
            comment.getParentId(), comment.getTaskId(), comment.getUserId(), comment.getCreatedAt(),
            comment.getLastUpdatedAt())).collect(Collectors.toList());
  }

  @Override
  public Comment save(Comment comment) {
    log.info("(save)comment: {}", comment);
    return commentRepository.save(comment);
  }

  @Override
  public CommentResponse replyComment(String userId, String taskId, String commentId, String text) {
    log.info("(replyComment)userId: {}, taskId: {}, commentId: {}", userId, taskId, commentId);
    Comment comment = new Comment();
    comment.setTaskId(taskId);
    comment.setUserId(userId);
    comment.setText(text);
    comment.setParentId(commentId);
    comment = commentRepository.save(comment);
    return CommentResponse.builder()
        .id(comment.getId())
        .text(comment.getText())
        .parentId(comment.getParentId())
        .taskId(comment.getTaskId())
        .userId(comment.getUserId())
        .createdAt(comment.getCreatedAt())
        .lastUpdatedAt(comment.getLastUpdatedAt())
        .build();
  }

  @Override
  public boolean existById(String id) {
    log.info("(existById)id: {}", id);
    return commentRepository.existsById(id);
  }
  @Transactional
  @Override
  public String deleteComment(String userId, String taskId, String commentId) {
    List<Comment> childComments = commentRepository.findAllByParentId(commentId);
    for (Comment childComment : childComments) {
      deleteComment(userId, taskId,childComment.getId());
    }
    commentRepository.deleteById(commentId);
    return "Successfull delete!";
  }

}
