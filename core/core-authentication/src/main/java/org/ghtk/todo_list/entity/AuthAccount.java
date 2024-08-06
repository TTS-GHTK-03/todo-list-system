package org.ghtk.todo_list.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@Entity
@Data
@NoArgsConstructor
@Table(name = "account")
public class AuthAccount extends BaseEntity {

  private String username;
  private String password;
  private Boolean isActivated = false;
  private Boolean isLockedPermanent= false;
  private Boolean isFirstLogin = true;

  public static AuthAccount of(String username, String password) {
    return AuthAccount.of(username, password, true, false, true);
  }

}
