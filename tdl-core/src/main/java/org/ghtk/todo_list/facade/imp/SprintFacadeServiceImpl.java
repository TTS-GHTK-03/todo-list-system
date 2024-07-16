package org.ghtk.todo_list.facade.imp;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.SprintStatus;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.exception.ProjectIdMismatchException;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.SprintNotFoundException;
import org.ghtk.todo_list.facade.SprintFacadeService;
import org.ghtk.todo_list.mapper.SprintMapper;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.ghtk.todo_list.model.response.SprintResponse;
import org.ghtk.todo_list.model.response.StartSprintResponse;
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

  @Override
  public StartSprintResponse startSprint(String projectId, String sprintId, String title,
      String startDate, String endDate) {
    log.info("(startSprint)projectId: {}, sprintId {}", projectId, sprintId);

    Sprint sprint = sprintService.findById(sprintId);
    if (!sprint.getProjectId().equals(projectId)) {
      log.error("(startSprint) projectId mismatch: sprintId {}, expected projectId {}, but found projectId {}",
          sprintId, projectId, sprint.getProjectId());
      throw new ProjectIdMismatchException();
    }

    sprint.setStatus(SprintStatus.START.toString());
    if(title!= null) sprint.setTitle(title);
    sprint.setStartDate(LocalDate.parse(startDate));
    sprint.setEndDate(LocalDate.parse(endDate));
    sprintService.save(sprint);

    return sprintMapper.toStartSprintResponse(sprint);
  }

  @Override
  public List<SprintResponse> getSprints(String projectId) {
    log.info("(getSprints)");

    if(!projectService.existById(projectId)) {
      log.error("(getSprints) project not found: projectId {}", projectId);
      throw new ProjectNotFoundException();
    }
    List<Sprint> sprints = sprintService.findSprintsByProjectId(projectId);
    log.info("(getSprints)sprints: {}", sprints);
    return sprintMapper.toSprintResponses(sprints);

  }
}
