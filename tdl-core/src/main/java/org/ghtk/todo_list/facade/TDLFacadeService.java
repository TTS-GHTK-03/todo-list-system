package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.repository.UserProjection;

public interface TDLFacadeService {

  List<TaskResponse> getAllTaskByProjectId(String userId, String projectId);

  TaskResponse getTaskByTaskId(String userId, String projectId, String taskId);

  TaskResponse updateStatusTask(String userId, String projectId, String taskId, String status);

}
