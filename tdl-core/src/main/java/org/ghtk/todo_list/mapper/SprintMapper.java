package org.ghtk.todo_list.mapper;

import java.time.LocalDate;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.ghtk.todo_list.model.response.StartSprintResponse;

public interface SprintMapper {
  CreateSprintResponse toCreateSprintResponse(Sprint sprint);
  StartSprintResponse toStartSprintResponse(Sprint sprint);
}
