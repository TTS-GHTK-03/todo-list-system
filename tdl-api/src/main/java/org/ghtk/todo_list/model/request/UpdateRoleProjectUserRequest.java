package org.ghtk.todo_list.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateRoleProjectUserRequest {

  @Schema(description = "Member id", example = "1")
  @NotBlank(message = "Member id is required")
  private String memberId;

  @Schema(description = "Role", example = "ADMIN")
  @NotBlank(message = "Role is required")
  private String role;

}
