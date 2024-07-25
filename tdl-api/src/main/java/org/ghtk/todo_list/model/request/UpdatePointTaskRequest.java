package org.ghtk.todo_list.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdatePointTaskRequest {

  @NotNull(message = "Trường điểm không thể trống")
  @Min(value = 0, message = "Điểm phải lớn hơn hoặc bằng 0")
  @Max(value = 5, message = "Điểm phải nhỏ hơn hoặc bằng 5")
  private Integer point;

}
