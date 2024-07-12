package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.model.response.TaskResponse;

public interface TDLFacadeService {

  List<TaskResponse> getAllTaskByProjectId(String projectId);
}
