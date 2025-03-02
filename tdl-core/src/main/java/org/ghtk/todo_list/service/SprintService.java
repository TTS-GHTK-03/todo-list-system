package org.ghtk.todo_list.service;

import java.util.List;
import org.ghtk.todo_list.entity.Sprint;

public interface SprintService {
  Sprint save(Sprint sprint);
  Sprint findById(String id);
  List<Sprint> findSprintsByProjectId(String projectId);
  List<Sprint> findSprintsByProjectIdAndStatus(String projectId, String status);
  boolean existById(String id);
  boolean existsByProjectIdAndTitle(String projectId, String title);
  void deleteAllByProjectId(String projectId);
  void deleteById(String id);
  void updateSprintComplete(String id);
}
