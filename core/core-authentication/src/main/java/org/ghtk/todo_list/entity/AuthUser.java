package org.ghtk.todo_list.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "AuthUser")
@NoArgsConstructor
@Table(name = "user")
public class AuthUser extends BaseEntity {

  private String firstName;
  private String middleName;
  private String lastName;
  private String email;
  private String phone;
  private LocalDate dateOfBirth;
  private String gender;
  private String address;
  private String accountId;

  public static AuthUser from(String email, String accountId) {
    var user = new AuthUser();
    user.setEmail(email);
    user.setAccountId(accountId);
    return user;
  }

  public static AuthUser from(String email) {
    var user = new AuthUser();
    user.setEmail(email);
    return user;
  }

}
