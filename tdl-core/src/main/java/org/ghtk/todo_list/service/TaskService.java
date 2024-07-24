package org.ghtk.todo_list.service;

import java.util.List;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.model.response.UpdateDueDateTaskResponse;

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
  boolean existByProjectIdAndTaskId(String projectId, String id);
  void deleteTask(String userId, String projectId, String taskId);
  void deleteAllByProjectId(String projectId);
  void deleteById(String id);
  void deleteAllBySprintId(String sprintId);

  void updateTaskTypeIdByTypeId(String defaultTypeId, String oldTypeId);
  void updateTitle(String taskId, String title);

  boolean existsByTypeId(String typeId);

  Task getTaskLastestByProjectId(String projectId);

  List<Task> getAllTaskAssigneesForUser(String userId);
  List<Task> searchTask(String searchValue, String userId, String projectId);
}
