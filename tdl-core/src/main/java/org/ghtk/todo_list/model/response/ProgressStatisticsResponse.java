package org.ghtk.todo_list.model.response;

import lombok.Data;

@Data
public class ProgressStatisticsResponse {

  private String sprintId;
  private Integer totalTask;
  private Integer completeTask;
  private String completionRate;

  public static ProgressStatisticsResponse from(String sprintId, Integer totalTask, Integer completeTask, String completionRate) {
    ProgressStatisticsResponse response = new ProgressStatisticsResponse();
    response.setSprintId(sprintId);
    response.setTotalTask(totalTask);
    response.setCompleteTask(completeTask);
    response.setCompletionRate(completionRate);
    return response;
  }
}
