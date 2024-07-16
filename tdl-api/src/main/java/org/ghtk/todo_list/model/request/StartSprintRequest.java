package org.ghtk.todo_list.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ghtk.todo_list.validation.ValidateLocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StartSprintRequest {

  private String title;

  @NotBlank(message = "Start date is required")
  @ValidateLocalDate
  private String startDate;

  @NotBlank(message = "End date is required")
  @ValidateLocalDate
  private String endDate;
}