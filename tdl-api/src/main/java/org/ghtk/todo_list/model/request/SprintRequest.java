package org.ghtk.todo_list.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ghtk.todo_list.validation.ValidateLocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SprintRequest {

  private String title;

  @ValidateLocalDate
  private String startDate;

  @ValidateLocalDate
  private String endDate;
}