package org.ghtk.todo_list.facade;

import java.time.LocalDate;
import java.util.List;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.ghtk.todo_list.model.response.ProgressStatisticsResponse;
import org.ghtk.todo_list.model.response.SprintResponse;
import org.ghtk.todo_list.model.response.StartSprintResponse;

public interface SprintFacadeService {
   CreateSprintResponse createSprintByProject(String projectId);

   StartSprintResponse startSprint(String projectId, String sprintId, String title, String startDate, String endDate);
   SprintResponse updateSprint(String projectId, String sprintId, String title, String startDate, String endDate);

   List<SprintResponse> getSprints(String projectId);
   List<SprintResponse> getSprintStatus(String projectId, String status);
   SprintResponse getSprint(String projectId, String id);

   ProgressStatisticsResponse getProgressStatistics(String projectId, String sprintId);
   void deleteSprint(String projectId, String id);
}
