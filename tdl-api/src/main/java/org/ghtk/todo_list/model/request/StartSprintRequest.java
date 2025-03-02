package org.ghtk.todo_list.model.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "Sprint title", example = "Sprint 1")
  private String title;

  @NotBlank(message = "Start date is required")
  @ValidateLocalDate
  @Schema(description = "Sprint start date", example = "2022-01-01")
  private String startDate;

  @NotBlank(message = "End date is required")
  @ValidateLocalDate
  @Schema(description = "Sprint end date", example = "2022-01-03")
  private String endDate;
}