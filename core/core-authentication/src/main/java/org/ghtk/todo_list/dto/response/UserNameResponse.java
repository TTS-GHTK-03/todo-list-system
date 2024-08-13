package org.ghtk.todo_list.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserNameResponse {
  private String firstName;
  private String middleName;
  private String lastName;
}
