package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.entity.TaskAssignees;
import org.ghtk.todo_list.model.response.TaskResponse;

public interface TaskFacadeService {

  List<TaskResponse> getAllTaskByProjectId(String userId, String projectId);

  TaskResponse getTaskByTaskId(String userId, String projectId, String taskId);

  TaskResponse updateStatusTask(String userId, String projectId, String taskId, String status);
  TaskResponse updateSprintTask(String userId, String projectId, String sprintId, String taskId);

  TaskAssignees agileTaskByUser(String email, String id);
}
