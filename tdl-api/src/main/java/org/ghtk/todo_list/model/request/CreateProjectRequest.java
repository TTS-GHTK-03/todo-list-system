package org.ghtk.todo_list.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateProjectRequest {
  @NotBlank(message = "Title is required")
  private String title;

  @NotBlank(message = "Role is required")
  private String role;
}
