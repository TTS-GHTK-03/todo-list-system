package org.ghtk.todo_list.model.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ghtk.todo_list.entity.Comment;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
  private String id;
  private String text;
  private String parentId;
  private String taskId;
  private String userId;
  private String username;
  private LocalDateTime createdAt;
  private LocalDateTime lastUpdatedAt;

  public static CommentResponse from(Comment comment, String username) {
    CommentResponse response = new CommentResponse();
    response.setId(comment.getId());
    response.setText(comment.getText());
    response.setParentId(comment.getParentId());
    response.setTaskId(comment.getTaskId());
    response.setUserId(comment.getUserId());
    response.setUsername(username);
    response.setCreatedAt(comment.getCreatedAt());
    response.setLastUpdatedAt(comment.getLastUpdatedAt());
    return response;
  }
}
