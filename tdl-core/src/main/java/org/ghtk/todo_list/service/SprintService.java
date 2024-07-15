package org.ghtk.todo_list.service;

import java.time.LocalDate;
import org.ghtk.todo_list.model.response.CreateSprintResponse;

public interface SprintService {
  CreateSprintResponse createSprintByProject(String projectId);

  CreateSprintResponse startSprint(String projectId, String sprintId, LocalDate startDate, LocalDate endDate);
}
