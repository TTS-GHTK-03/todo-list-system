package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.entity.TaskAssignees;
import org.ghtk.todo_list.model.response.SprintsProjectDetailResponse;
import org.ghtk.todo_list.model.response.TaskDetailResponse;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.model.response.UpdateDueDateTaskResponse;

public interface TaskFacadeService {

  List<TaskResponse> getAllTaskByProjectParticipant(String userId);
  List<TaskDetailResponse> getAllTaskByProjectId(String userId, String projectId);

  TaskDetailResponse getTaskByTaskId(String userId, String projectId, String taskId);

  TaskResponse updateStatusTask(String userId, String projectId, String taskId, String status);
  TaskResponse updatePointTask(String userId, String projectId, String taskId, int point);

  TaskResponse updateSprintTask(String userId, String projectId, String sprintId, String taskId);

  TaskAssignees agileTaskByUser(String email, String id);
  TaskResponse cloneTask(String userId, String projectId, String taskId);
  UpdateDueDateTaskResponse updateStartDateDueDateTask(String userId, String projectId,
      String sprintId, String taskId, String dueDate);
  List<TaskDetailResponse> getAllTaskByProjectIdAndStatus(String userId,String projectId, String status);
  List<TaskResponse> getAllBySprintId(String projectId, String sprintId);
  TaskDetailResponse createTask(String userId, String projectId, String title);
  void deleteTask(String userId, String projectId, String taskId);
  TaskResponse updateTitleTask(String userId, String projectId, String taskId, String title);
  List<TaskResponse> getAllTaskAssigneesForUser(String userId);
  List<TaskDetailResponse> searchTask(String searchValue, String typeId, String labelId, String status,
      String assignee, String userId, String projectId, String sprintId);
  List<SprintsProjectDetailResponse> getAllTaskByAllSprint(String projectId);
}
