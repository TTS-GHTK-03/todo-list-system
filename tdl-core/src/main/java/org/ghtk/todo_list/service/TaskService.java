package org.ghtk.todo_list.service;

import java.util.List;
import org.ghtk.todo_list.constant.TaskStatus;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.model.response.UpdateDueDateTaskResponse;
import org.ghtk.todo_list.repository.UserProjection;

public interface TaskService {

  List<TaskResponse> getAllTasksByProjectId(String projectId);

  TaskResponse findById(String taskId, String userId);

  TaskResponse updateStatus(String taskId, String taskStatus, String userId);

  String getUserIdById(String taskId);

  TaskResponse updateSprintId(String projectId, String taskId, String sprintId, String userId);

  boolean existsByUserIdAndTaskId(String userId, String taskId);
  boolean existById(String id);

  Task findById(String taskId);

  Task save(Task task);

  UpdateDueDateTaskResponse updateDueDate(String projectId, String sprintId, String taskId, String dueDate);

  List<Task> getAllTasksByProjectIdAndStatus(String projectId, String status);

  List<Task> getAllBySprintId(String sprintId);

  boolean existsBySprintId(String sprintId);
}
