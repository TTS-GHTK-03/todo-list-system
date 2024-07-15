package org.ghtk.todo_list.service;

import java.time.LocalDate;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.ghtk.todo_list.model.response.StartSprintResponse;

public interface SprintService {
  CreateSprintResponse createSprintByProject(String projectId);

  StartSprintResponse startSprint(String projectId, String sprintId, String title, LocalDate startDate, LocalDate endDate);
}
