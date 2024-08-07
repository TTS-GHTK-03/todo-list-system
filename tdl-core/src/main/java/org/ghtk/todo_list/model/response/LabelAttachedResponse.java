package org.ghtk.todo_list.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabelAttachedResponse {
  private String id;
  private String taskId;
  private String labelId;
}
