package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.Sprint;
import java.time.LocalDate;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.ghtk.todo_list.model.response.StartSprintResponse;

public interface SprintService {
  Sprint save(Sprint sprint);
  Sprint findById(String id);
}
