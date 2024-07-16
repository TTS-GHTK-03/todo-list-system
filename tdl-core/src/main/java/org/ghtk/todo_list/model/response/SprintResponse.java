package org.ghtk.todo_list.model.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprintResponse {
  private String id;
  private String title;
  private String status;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDateTime createdAt;
  private LocalDateTime lastUpdatedAt;
}
