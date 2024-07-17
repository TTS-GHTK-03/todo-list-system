package org.ghtk.todo_list.mapper;

import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.model.response.UpdateDueDateTaskResponse;

public interface TaskMapper {
  UpdateDueDateTaskResponse toUpdateDueDateTaskResponse(Task task);
}
