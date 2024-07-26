package org.ghtk.todo_list.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ghtk.todo_list.validation.ValidateLocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDueDateTaskRequest {

  @NotBlank(message = "Status task key is required")
  @Schema(description = "Status task key", example = "abc")
  private String statusTaskKey;

  @NotBlank(message = "Due date is required")
  @ValidateLocalDate
  @Schema(description = "Due date", example = "2022-09-01")
  private String dueDate;
}
