package org.ghtk.todo_list.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.ghtk.todo_list.validation.ValidateGender;
import org.ghtk.todo_list.validation.ValidateLocalDate;
import org.ghtk.todo_list.validation.ValidatePhone;

@Data
public class UpdateInformationRequest {

  @Schema(description = "First name", example = "John")
  private String firstName;
  @Schema(description = "Middle name", example = "Doe")
  private String middleName;
  @Schema(description = "Last name", example = "Doe")
  private String lastName;
  @ValidatePhone
  @Schema(description = "Phone", example = "+84123456789")
  private String phone;
  @ValidateLocalDate
  @Schema(description = "Date of birth", example = "1990-01-01")
  private String dateOfBirth;
  @ValidateGender
  @Schema(description = "Gender", example = "MALE")
  private String gender;
  @Schema(description = "Address", example = "123 Main Street")
  private String address;
}
