package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.model.response.CreateSprintResponse;

public interface SprintService {
  Sprint save(Sprint sprint);
}
