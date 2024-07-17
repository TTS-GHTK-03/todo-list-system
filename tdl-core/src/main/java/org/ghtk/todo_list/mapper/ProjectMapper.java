package org.ghtk.todo_list.mapper;

import java.time.LocalDateTime;
import org.ghtk.todo_list.entity.Project;

public interface ProjectMapper {
  Project toProject(String title, String key);
}
