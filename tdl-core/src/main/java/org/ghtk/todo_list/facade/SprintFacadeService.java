package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.model.response.CreateSprintResponse;

public interface SprintFacadeService {
   CreateSprintResponse createSprintByProject(String projectId);
}
