package org.ghtk.todo_list.mapper;

import java.util.List;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.model.response.TaskDetailResponse;
import org.ghtk.todo_list.model.response.TaskResponse;

public interface TaskMapper {

  List<TaskResponse> toTaskResponses(List<Task> tasks);

  List<TaskDetailResponse> toTaskDetailResponses(List<Task> tasks);
}
