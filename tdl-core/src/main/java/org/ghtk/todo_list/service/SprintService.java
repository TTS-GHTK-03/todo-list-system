package org.ghtk.todo_list.service;

import java.util.List;
import org.ghtk.todo_list.entity.Sprint;

public interface SprintService {
  Sprint save(Sprint sprint);
  Sprint findById(String id);
  List<Sprint> findSprintsByProjectId(String projectId);
  List<Sprint> findSprintsByProjectIdAndStatus(String projectId, String status);
  Sprint findSprintByProjectIdAndSprintId(String projectId, String sprintId);
  boolean existById(String id);
  boolean existsByProjectIdAndTitle(String projectId, String title);
}
