package org.ghtk.todo_list.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateTaskRequest {
  @NotBlank(message = "Title is required")
  @Schema(description = "Title task", example = "Task 1")
  private String title;
}
