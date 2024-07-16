package org.ghtk.todo_list.facade;

import java.time.LocalDate;
import java.util.List;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.ghtk.todo_list.model.response.SprintResponse;
import org.ghtk.todo_list.model.response.StartSprintResponse;

public interface SprintFacadeService {
   CreateSprintResponse createSprintByProject(String projectId);

   StartSprintResponse startSprint(String projectId, String sprintId, String title, String startDate, String endDate);

   List<SprintResponse> getSprints(String projectId);
}
