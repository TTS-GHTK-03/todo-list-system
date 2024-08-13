package org.ghtk.todo_list.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TypeRequest {

  @NotBlank(message = "Title is required")
  @Schema(description = "Title of type", example = "Work")
  private String title;

  @NotBlank(message = "Image is required")
  @Schema(description = "Image of type", example = "work.png")
  private String image;

  @Schema(description = "Description of type", example = "Work")
  private String description;
}
