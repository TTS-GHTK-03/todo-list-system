package org.ghtk.todo_list.facade.imp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.SprintStatus;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.facade.SprintFacadeService;
import org.ghtk.todo_list.mapper.SprintMapper;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.SprintService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SprintFacadeServiceImpl implements SprintFacadeService {

  private final SprintService sprintService;
  private final SprintMapper sprintMapper;
  private final ProjectService projectService;

  @Transactional
  @Override
  public CreateSprintResponse createSprintByProject(String projectId) {
    log.info("(createSprintByProject)");
    Project project = projectService.getProjectById(projectId);

    Long counter = (project.getCountSprint() != null ? project.getCountSprint() : 0L) + 1;
    log.info("(createSprintByProject)count: {}", counter);

    Sprint sprint = new Sprint();
    sprint.setTitle(project.getKeyProject() + " Sprint " + counter);
    sprint.setStatus(SprintStatus.TODO.toString());
    sprint.setProjectId(project.getId());
    sprint = sprintService.save(sprint);

    projectService.updateCountSprint(project.getId(), counter);
    return sprintMapper.toCreateSprintResponse(sprint);
  }
}
