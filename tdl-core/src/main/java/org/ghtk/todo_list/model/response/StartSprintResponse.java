package org.ghtk.todo_list.model.response;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.*;
import org.ghtk.todo_list.validation.ValidateLocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartSprintResponse {
  private String id;
  private String title;
  private String status;
  private LocalDate startDate;
  private LocalDate endDate;
}
