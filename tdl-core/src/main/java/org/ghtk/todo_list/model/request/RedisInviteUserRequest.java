package org.ghtk.todo_list.model.request;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RedisInviteUserRequest implements Serializable {

  @NotBlank(message = "ProjectId is required")
  private String projectId;

  @NotBlank(message = "Role is required")
  private String role;
}
