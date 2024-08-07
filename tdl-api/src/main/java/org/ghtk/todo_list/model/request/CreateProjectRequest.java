package org.ghtk.todo_list.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateProjectRequest {
  @NotBlank(message = "Title is required")
  @Schema(description = "Project title", example = "GHTK-2024")
  private String title;
}
