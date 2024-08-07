package org.ghtk.todo_list.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.ghtk.todo_list.validation.ValidateEmail;

@Data
public class VerifyEmailRequest {

  @NotBlank(message = "Email is required")
  @ValidateEmail
  @Schema(description = "Email", example = "xg6g6@gmail.com")
  private String email;
}
