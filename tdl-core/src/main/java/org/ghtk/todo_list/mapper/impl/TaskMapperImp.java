package org.ghtk.todo_list.mapper.impl;

import java.time.LocalDateTime;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.mapper.TaskMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImp implements TaskMapper {

  @Override
  public Task toTask(String title, LocalDateTime createdAt, LocalDateTime lastUpdatedAt) {
    Task task = new Task();
    task.setTitle(title);
    task.setCreatedAt(createdAt);
    task.setLastUpdatedAt(lastUpdatedAt);

    return task;
  }
}
