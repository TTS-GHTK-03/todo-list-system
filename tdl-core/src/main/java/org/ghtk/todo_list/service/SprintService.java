package org.ghtk.todo_list.service;

import org.ghtk.todo_list.model.response.CreateSprintResponse;

public interface SprintService {
  CreateSprintResponse createSprintByProject(String projectId);
}
