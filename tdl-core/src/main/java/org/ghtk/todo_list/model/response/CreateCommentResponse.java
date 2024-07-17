package org.ghtk.todo_list.model.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentResponse {
  private String id;
  private String text;
  private String taskId;
  private String userId;
  private LocalDateTime createdAt;
  private LocalDateTime lastUpdatedAt;
}
