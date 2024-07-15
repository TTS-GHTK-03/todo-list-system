package org.ghtk.todo_list.dto.request;

import lombok.Data;
import org.ghtk.todo_list.validation.ValidateGender;
import org.ghtk.todo_list.validation.ValidateLocalDate;
import org.ghtk.todo_list.validation.ValidatePhone;

@Data
public class UpdateInformationRequest {

  private String firstName;
  private String middleName;
  private String lastName;
  @ValidatePhone
  private String phone;
  @ValidateLocalDate
  private String dateOfBirth;
  @ValidateGender
  private String gender;
  private String address;
}
