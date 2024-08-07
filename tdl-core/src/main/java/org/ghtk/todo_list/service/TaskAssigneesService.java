package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.TaskAssignees;

public interface TaskAssigneesService {

  String findUserIdByTaskId(String taskId);
  TaskAssignees findById(String id);
  TaskAssignees save(TaskAssignees taskAssignees);
  boolean existsByUserIdAndTaskId(String userId, String taskId);
  void deleteAllByTaskId(String taskId);
  void updateTaskAssigneesByUserIdAndProjectId(String userId, String memberId, String projectId);
  TaskAssignees findByTaskId(String taskId);
}
