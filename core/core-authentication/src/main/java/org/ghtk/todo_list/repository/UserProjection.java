package org.ghtk.todo_list.repository;

import lombok.Data;

@Data
public class UserProjection {

  private String firstName;
  private String middleName;
  private String lastName;
  private String email;

  public UserProjection(String firstName, String middleName, String lastName, String email) {
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.email = email;
  }
}
