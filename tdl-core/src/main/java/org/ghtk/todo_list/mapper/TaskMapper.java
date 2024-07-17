package org.ghtk.todo_list.mapper;

import java.util.List;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.model.response.UpdateDueDateTaskResponse;

public interface TaskMapper {
  UpdateDueDateTaskResponse toUpdateDueDateTaskResponse(Task task);

  List<TaskResponse> toTaskResponses(List<Task> tasks);

}
