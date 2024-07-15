package org.ghtk.todo_list.dto.response;

import java.time.LocalDate;
import lombok.Data;
import org.ghtk.todo_list.entity.AuthUser;

@Data
public class AuthUserResponse {

  private String id;
  private String firstName;
  private String middleName;
  private String lastName;
  private String email;
  private String phone;
  private LocalDate dateOfBirth;
  private String gender;
  private String address;

  public static AuthUserResponse from(AuthUser authUser) {
    AuthUserResponse response = new AuthUserResponse();
    response.setId(authUser.getId());
    response.setFirstName(authUser.getFirstName());
    response.setMiddleName(authUser.getMiddleName());
    response.setLastName(authUser.getLastName());
    response.setEmail(authUser.getEmail());
    response.setPhone(authUser.getPhone());
    response.setDateOfBirth(authUser.getDateOfBirth());
    response.setGender(authUser.getGender());
    response.setAddress(authUser.getAddress());
    return response;
  }
}
