package org.ghtk.todo_list.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

  private String id;
  private String username;
  private String firstName;
  private String middleName;
  private String lastName;
  private String email;
  private String role;
}
