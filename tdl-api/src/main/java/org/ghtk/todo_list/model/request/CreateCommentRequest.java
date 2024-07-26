package org.ghtk.todo_list.model.request;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateCommentRequest {

  @NotBlank(message = "Text is required")
  @Schema(description = "Comment text", example = "My comment")
  private String text;
}

