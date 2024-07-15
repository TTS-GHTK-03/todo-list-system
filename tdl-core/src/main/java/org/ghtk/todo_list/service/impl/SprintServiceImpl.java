package org.ghtk.todo_list.service.impl;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.SprintStatus;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.mapper.SprintMapper;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.ghtk.todo_list.repository.ProjectRepository;
import org.ghtk.todo_list.repository.SprintRepository;
import org.ghtk.todo_list.service.SprintService;

@Slf4j
@RequiredArgsConstructor
public class SprintServiceImpl implements SprintService {

  private final SprintRepository sprintRepository;
  private final ProjectRepository projectRepository;
  private final SprintMapper sprintMapper;

  @Override
  public CreateSprintResponse createSprintByProject(String projectId) {
    log.info("(createSprintByProject)projectId: {}", projectId);
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> {
          log.error("(createSprintByProject)projectId: {} not found", projectId);
          throw new ProjectNotFoundException();
        });
    Sprint sprint = new Sprint();
    sprint.setTitle(project.getKeyProject() + " Sprint " + 1);
    sprint.setStatus(SprintStatus.TODO.toString());
    sprint.setProjectId(project.getId());
    sprint = sprintRepository.save(sprint);

    log.info("(createSprintByProject)sprint: {}", sprint);
    return sprintMapper.toCreateSprintResponse(sprint);
  }

  @Override
  public CreateSprintResponse startSprint(String projectId, String sprintId, LocalDate startDate,
      LocalDate endDate) {

    return null;
  }
}
