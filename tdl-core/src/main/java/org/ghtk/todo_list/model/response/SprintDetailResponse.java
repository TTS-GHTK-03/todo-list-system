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
public class SprintDetailResponse {

  private String sprintId;
  private String sprintTitle;
  private String sprintStatus;
}
