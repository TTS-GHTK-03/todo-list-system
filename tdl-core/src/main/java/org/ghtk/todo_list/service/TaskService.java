package org.ghtk.todo_list.service;

import java.util.List;
import org.ghtk.todo_list.constant.TaskStatus;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.model.response.UpdateDueDateTaskResponse;
import org.ghtk.todo_list.repository.UserProjection;

public interface TaskService {

  List<TaskResponse> getAllTasksByProjectId(String projectId);

  TaskResponse findById(String taskId, UserProjection userProjection);

  TaskResponse updateStatus(String taskId, String taskStatus, UserProjection userProjection);

  String getUserIdById(String taskId);

  TaskResponse updateSprintId(String projectId, String taskId, String sprintId, UserProjection userProjection);

  boolean existById(String taskId);

  UpdateDueDateTaskResponse updateDueDate(String projectId, String sprintId, String taskId, String dueDate);
}
