package org.ghtk.todo_list.mapper;

import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.model.response.CreateSprintResponse;

public interface SprintMapper {
  CreateSprintResponse toCreateSprintResponse(Sprint sprint);
}
