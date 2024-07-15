package org.ghtk.todo_list.mapper;

import java.time.LocalDateTime;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.Task;

public interface TaskMapper {

  Task toTask(String title, LocalDateTime createdAt, LocalDateTime lastUpdatedAt);
}
