package org.ghtk.todo_list.service;

import java.util.List;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.model.response.TaskResponse;

public interface TaskService {

  List<TaskResponse> getAllTasksByProjectId(String projectId);

  TaskResponse getTaskByTaskId(String taskId);

  boolean existsById(String taskId);
}
