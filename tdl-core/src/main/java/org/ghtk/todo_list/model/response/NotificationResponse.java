package org.ghtk.todo_list.model.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ghtk.todo_list.dto.response.UserNameResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

  private String id;
  private String action;
  private String sprintTitle;
  private String taskTitle;
  private UserNameResponse userNameResponse;
  private LocalDateTime createdAt;
}
