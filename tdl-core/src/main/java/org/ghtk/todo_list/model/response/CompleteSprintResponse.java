package org.ghtk.todo_list.model.response;

import lombok.Data;

@Data
public class CompleteSprintResponse {

  private Integer countCompleteTask;
  private Integer countFailedTasks;

  public static CompleteSprintResponse from(Integer countCompleteTask, Integer countFailedTasks) {
    CompleteSprintResponse response = new CompleteSprintResponse();
    response.setCountCompleteTask(countCompleteTask);
    response.setCountFailedTasks(countFailedTasks);
    return response;
  }
}
