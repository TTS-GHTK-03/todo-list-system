package org.ghtk.todo_list.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateTaskRequest {

  @NotBlank(message = "Title is required")
  private String title;

}
