package org.ghtk.todo_list.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ghtk.todo_list.dto.response.UserResponse;

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
  private UserResponse userResponse;
  private SprintDetailResponse sprintDetailResponse;
  private TypeResponse typeResponse;
  private List<LabelResponse> labelResponseList;
}
