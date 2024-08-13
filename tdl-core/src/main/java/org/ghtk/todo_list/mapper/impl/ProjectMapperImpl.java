package org.ghtk.todo_list.mapper.impl;

import java.time.LocalDateTime;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.mapper.ProjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapperImpl implements ProjectMapper {

  @Override
  public Project toProject(String title, String key) {
    Project project = new Project();
    project.setTitle(title);
    project.setKeyProject(key);
    project.setCounterTask(0);
    return project;
  }
}
