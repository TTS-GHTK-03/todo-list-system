package org.ghtk.todo_list.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.sql.Time;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ghtk.todo_list.validation.ValidateEmail;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShareUserRequest {

  @NotBlank(message = "Email is required")
  @ValidateEmail
  @Schema(description = "Email of user", example = "pPQpL@example.com")
  private String email;

  @Schema(description = "Role of invited user", example = "ADMIN")
  @NotBlank(message = "Role is required")
  private String role;

  @Schema(description = "Expire time", example = "1")

  private long expireTime;
}
