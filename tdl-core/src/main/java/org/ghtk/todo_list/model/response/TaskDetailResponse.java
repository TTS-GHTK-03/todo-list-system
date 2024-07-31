package org.ghtk.todo_list.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class TaskDetailResponse {

  private String id;
  private String title;
  private Integer point;
  private String status;
  private String keyProjectTask;
  private String userId;
  private String sprintId;
  private String sprintTitle;
}
