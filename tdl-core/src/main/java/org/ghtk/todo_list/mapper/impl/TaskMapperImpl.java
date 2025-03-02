package org.ghtk.todo_list.mapper.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.ghtk.todo_list.dto.response.UserResponse;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.mapper.TaskMapper;
import org.ghtk.todo_list.model.response.SprintDetailResponse;
import org.ghtk.todo_list.model.response.TaskDetailResponse;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.model.response.TypeResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements TaskMapper {

  @Override
  public List<TaskResponse> toTaskResponses(List<Task> tasks) {
    return tasks.stream().map(task -> {
      return TaskResponse.builder()
          .id(task.getId())
          .title(task.getTitle())
          .point(task.getPoint())
          .status(task.getStatus())
          .keyProjectTask(task.getKeyProjectTask())
          .build();
    }).collect(Collectors.toList());
  }

  @Override
  public List<TaskDetailResponse> toTaskDetailResponses(List<Task> tasks) {
    return tasks.stream().map(task -> {
      return TaskDetailResponse.builder()
          .id(task.getId())
          .title(task.getTitle())
          .point(task.getPoint())
          .status(task.getStatus())
          .keyProjectTask(task.getKeyProjectTask())
          .userResponse(null)
          .sprintDetailResponse(SprintDetailResponse.of(task.getSprintId(), null, null))
          .typeResponse(TypeResponse.of(task.getTypeId(), null, null, null))
          .labelResponseList(null)
          .build();
    }).collect(Collectors.toList());
  }

}
