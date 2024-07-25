package org.ghtk.todo_list.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdatePointTaskRequest {

  @NotNull
  @Min(0)
  @Max(5)
  private Integer point;

}
