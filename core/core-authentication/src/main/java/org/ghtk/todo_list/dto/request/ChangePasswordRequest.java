package org.ghtk.todo_list.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.ghtk.todo_list.validation.ValidatePassword;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

  @NotBlank(message = "old password is required")
  @ValidatePassword
  @Schema(description = "Old Password", example = "Password2024@")
  private String oldPassword;
  @NotBlank(message = "new password is required")
  @ValidatePassword
  @Schema(description = "New Password", example = "Password2024@")
  private String newPassword;
  @NotBlank(message = "confirm password is required")
  @ValidatePassword
  @Schema(description = "Confirm Password", example = "Password2024@")
  private String confirmPassword;

}
