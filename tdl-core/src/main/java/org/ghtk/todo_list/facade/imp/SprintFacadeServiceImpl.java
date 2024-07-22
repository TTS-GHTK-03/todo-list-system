package org.ghtk.todo_list.facade.imp;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.SprintStatus;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.exception.InvalidDateRangeException;
import org.ghtk.todo_list.exception.ProjectIdMismatchException;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.SprintStatusNotFoundException;
import org.ghtk.todo_list.facade.SprintFacadeService;
import org.ghtk.todo_list.mapper.SprintMapper;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.ghtk.todo_list.model.response.ProgressStatisticsResponse;
import org.ghtk.todo_list.model.response.SprintResponse;
import org.ghtk.todo_list.model.response.StartSprintResponse;
import org.ghtk.todo_list.service.ActivityLogService;
import org.ghtk.todo_list.service.CommentService;
import org.ghtk.todo_list.service.LabelAttachedService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.SprintProgressService;
import org.ghtk.todo_list.service.SprintService;
import org.ghtk.todo_list.service.TaskAssigneesService;
import org.ghtk.todo_list.service.TaskService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SprintFacadeServiceImpl implements SprintFacadeService {

  private final SprintService sprintService;
  private final SprintMapper sprintMapper;
  private final ProjectService projectService;
  private final SprintProgressService sprintProgressService;
  private final TaskService taskService;

  private final CommentService commentService;
  private final LabelAttachedService labelAttachedService;
  private final ActivityLogService activityLogService;
  private final TaskAssigneesService taskAssigneesService;

  @Transactional
  @Override
  public CreateSprintResponse createSprintByProject(String projectId) {
    log.info("(createSprintByProject)");
    Project project = projectService.getProjectById(projectId);

    int count = 1;
    String title = project.getKeyProject() + " Sprint " + count;
    while (sprintService.existsByProjectIdAndTitle(projectId, title)) {
      count++;
      title = project.getKeyProject() + " Sprint " + count;
    }

    Sprint sprint = new Sprint();
    sprint.setTitle(title);
    sprint.setStatus(SprintStatus.TODO.toString());
    sprint.setProjectId(project.getId());
    sprint = sprintService.save(sprint);

    return sprintMapper.toCreateSprintResponse(sprint);
  }

  @Override
  public StartSprintResponse startSprint(String projectId, String sprintId, String title,
      String startDate, String endDate) {
    log.info("(startSprint)projectId: {}, sprintId {}", projectId, sprintId);

    if (isValidDateRange(LocalDate.parse(startDate), LocalDate.parse(endDate))) {
      log.error("(startSprint)invalid date range: startDate {}, endDate: {}", startDate, endDate);
      throw new InvalidDateRangeException();
    }

    Sprint sprint = sprintService.findById(sprintId);
    if (!sprint.getProjectId().equals(projectId)) {
      log.error(
          "(startSprint) projectId mismatch: sprintId {}, expected projectId {}, but found projectId {}",
          sprintId, projectId, sprint.getProjectId());
      throw new ProjectIdMismatchException();
    }

    sprint.setStatus(SprintStatus.START.toString());
    if (title != null && !title.isEmpty()) {
      sprint.setTitle(title);
    }
    sprint.setStartDate(LocalDate.parse(startDate));
    sprint.setEndDate(LocalDate.parse(endDate));
    sprintService.save(sprint);

    return sprintMapper.toStartSprintResponse(sprint);
  }

  @Override
  public SprintResponse updateSprint(String projectId, String sprintId, String title,
      String startDate, String endDate) {
    log.info("(updateSprint)projectId: {}, sprintId {}", projectId, sprintId);

    if (isValidDateRange(LocalDate.parse(startDate), LocalDate.parse(endDate))) {
      log.error("(updateSprint)invalid date range: startDate {}, endDate: {}", startDate, endDate);
      throw new InvalidDateRangeException();
    }

    Sprint sprint = sprintService.findById(sprintId);
    if (!sprint.getProjectId().equals(projectId)) {
      log.error(
          "(updateSprint) projectId mismatch: sprintId {}, expected projectId {}, but found projectId {}",
          sprintId, projectId, sprint.getProjectId());
      throw new ProjectIdMismatchException();
    }

    if (title != null && !title.isEmpty()) {
      sprint.setTitle(title);
    }
    sprint.setStartDate(LocalDate.parse(startDate));
    sprint.setEndDate(LocalDate.parse(endDate));
    sprintService.save(sprint);

    return sprintMapper.toSprintResponse(sprint);
  }

  @Override
  public List<SprintResponse> getSprints(String projectId) {
    log.info("(getSprints)");

    if (!projectService.existById(projectId)) {
      log.error("(getSprints) project not found: projectId {}", projectId);
      throw new ProjectNotFoundException();
    }
    List<Sprint> sprints = sprintService.findSprintsByProjectId(projectId);
    log.info("(getSprints)sprints: {}", sprints);
    return sprintMapper.toSprintResponses(sprints);

  }

  @Override
  public List<SprintResponse> getSprintStatus(String projectId, String status) {
    log.info("(getSprintStatus)");

    String statusFormat = status.trim().toUpperCase();
    if (!SprintStatus.isValid(statusFormat)) {
      log.error("(getSprintStatus) status sprint not found: status {}", status);
      throw new SprintStatusNotFoundException();
    }
    if (!projectService.existById(projectId)) {
      log.error("(getSprintStatus) project not found: projectId {}", projectId);
      throw new ProjectNotFoundException();
    }
    List<Sprint> sprints = sprintService.findSprintsByProjectIdAndStatus(projectId, statusFormat);
    log.info("(getSprintStatus)sprints: {}", sprints);
    return sprintMapper.toSprintResponses(sprints);
  }

  @Override
  public SprintResponse getSprint(String projectId, String id) {
    log.info("(getSprint)");
    Sprint sprint = sprintService.findById(id);
    if (!sprint.getProjectId().equals(projectId)) {
      log.error(
          "(getSprint) projectId mismatch: sprintId {}, expected projectId {}, but found projectId {}",
          id, projectId, sprint.getProjectId());
      throw new ProjectIdMismatchException();
    }
    log.info("(getSprint)sprint: {}", sprint);
    return sprintMapper.toSprintResponse(sprint);
  }

  @Override
  public ProgressStatisticsResponse getProgressStatistics(String projectId, String sprintId) {
    log.info("(getProgressStatistics) projectId: {}, sprintId {}", projectId, sprintId);
    var sprintProgress = sprintProgressService.findBySprintId(sprintId);
    int totalTask = sprintProgress.getTotalTask();
    double completionRate;
    if (totalTask == 0) {
      completionRate = 0.0;
    }

    completionRate = ((double) sprintProgress.getCompleteTask() / totalTask) * 100;
    return ProgressStatisticsResponse
        .from(sprintProgress.getSprintId(),
            sprintProgress.getTotalTask(),
            sprintProgress.getCompleteTask(),
            (completionRate + "%"));
  }

  @Override
  @Transactional
  public void deleteSprint(String projectId, String id) {
    log.info("(deleteSprint) projectId: {}, sprintId {}", projectId, id);
    sprintProgressService.deleteAllBySprintId(id);
    List<Task> tasks = taskService.getAllBySprintId(id);
    for (Task o : tasks) {
      taskAssigneesService.deleteAllByTaskId(o.getId());
      commentService.deleteAllCommentByTaskId(o.getId());
      labelAttachedService.deleteAllByTaskId(o.getId());
      activityLogService.deleteAllByTaskId(o.getId());
    }
    taskService.deleteAllBySprintId(id);
    sprintService.deleteById(id);
  }

  private boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
    return !startDate.isBefore(endDate);
  }
}
