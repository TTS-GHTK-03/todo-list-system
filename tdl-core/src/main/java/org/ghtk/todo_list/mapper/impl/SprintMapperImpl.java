package org.ghtk.todo_list.mapper.impl;

import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.mapper.SprintMapper;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.springframework.stereotype.Component;

@Component
public class SprintMapperImpl implements SprintMapper {

  @Override
  public CreateSprintResponse toCreateSprintResponse(Sprint sprint) {
    return CreateSprintResponse.builder()
        .id(sprint.getId())
        .title(sprint.getTitle())
        .status(sprint.getStatus())
        .build();
  }
}
