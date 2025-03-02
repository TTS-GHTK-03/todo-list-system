package org.ghtk.todo_list.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.ghtk.todo_list.validation.ValidatePassword;

@Data
public class LoginRequest {

  @NotBlank(message = "Username is required")
  @Size(min = 5, max = 20, message = "username must be between 8 and 20 character")
  @Schema(description = "Username", example = "username")
  private String username;
  @NotBlank(message = "Password is required")
  @ValidatePassword
  @Schema(description = "Password", example = "Password2024@")
  private String password;
}
