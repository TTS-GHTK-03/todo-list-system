package org.ghtk.todo_list.model.request;

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
  private Integer point;

}
