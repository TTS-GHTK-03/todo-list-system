package org.ghtk.todo_list.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateProjectRequest {

  @NotBlank(message = "Title is required")
  private String title;

  @NotBlank(message = "Key project is required")
  private String keyProject;
}
