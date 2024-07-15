package org.ghtk.todo_list.model.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ghtk.todo_list.validation.ValidateLocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StartSprintRequest {

  @NotBlank(message = "Title is required")
  private String title;

  @NotBlank(message = "Start date is required")
  @ValidateLocalDate
  private LocalDate startDate;

  @NotBlank(message = "End date is required")
  @ValidateLocalDate
  private LocalDate endDate;
}
