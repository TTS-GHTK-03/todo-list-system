package org.ghtk.todo_list.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdatePointTaskRequest {

  @NotNull(message = "Point is required!")
  @Min(value = 0, message = "Point must be greater or equal 0!")
  @Max(value = 5, message = "Point must be less or equal 5!")
  @Schema(description = "Point of task", example = "2")
  private Integer point;

}
