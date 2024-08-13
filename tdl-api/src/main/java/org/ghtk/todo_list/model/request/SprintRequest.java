package org.ghtk.todo_list.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ghtk.todo_list.validation.ValidateLocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SprintRequest {

  @Schema(description = "Sprint title", example = "Sprint 1")
  private String title;

  @Schema(description = "Sprint start date", example = "2022-12-01")
  @ValidateLocalDate
  private String startDate;

  @Schema(description = "Sprint end date", example = "2022-12-31")
  @ValidateLocalDate
  private String endDate;
}