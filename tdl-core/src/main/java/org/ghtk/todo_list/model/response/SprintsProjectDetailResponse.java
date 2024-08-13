package org.ghtk.todo_list.model.response;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SprintsProjectDetailResponse {
  private String id;
  private String title;
  private LocalDate startDate;
  private LocalDate endDate;
  private List<TaskResponse> taskResponseList;
}
