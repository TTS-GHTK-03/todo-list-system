package org.ghtk.todo_list.facade.imp;

import static org.ghtk.todo_list.constant.ActivityLogConstant.SprintAction.*;
import static org.ghtk.todo_list.constant.TaskStatus.*;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.SprintStatus;
import org.ghtk.todo_list.constant.TaskStatus;
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.entity.SprintProgress;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.exception.InvalidDateRangeException;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.SprintNotExistProjectException;
import org.ghtk.todo_list.exception.SprintNotFoundException;
import org.ghtk.todo_list.exception.SprintStatusNotFoundException;
import org.ghtk.todo_list.exception.SprintStatusTodoInvalidException;
import org.ghtk.todo_list.facade.SprintFacadeService;
import org.ghtk.todo_list.mapper.SprintMapper;
import org.ghtk.todo_list.model.response.CompleteSprintResponse;
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
  public CreateSprintResponse createSprintByProject(String userId, String projectId) {
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

    SprintProgress sprintProgress = new SprintProgress();
    sprintProgress.setSprintId(sprint.getId());
    sprintProgress.setTotalTask(0);
    sprintProgress.setCompleteTask(0);
    sprintProgressService.save(sprintProgress);

    var notification = new ActivityLog();
    notification.setAction(CREATE_SPRINT);
    notification.setUserId(userId);
    notification.setSprintId(sprint.getId());
    activityLogService.create(notification);

    return sprintMapper.toCreateSprintResponse(sprint);
  }

  @Override
  public StartSprintResponse startSprint(String userId, String projectId, String sprintId, String title,
      String startDate, String endDate) {
    log.info("(startSprint)projectId: {}, sprintId {}", projectId, sprintId);

    if (isValidDateRange(LocalDate.parse(startDate), LocalDate.parse(endDate))) {
      log.error("(startSprint)invalid date range: startDate {}, endDate: {}", startDate, endDate);
      throw new InvalidDateRangeException();
    }

    validateProjectId(projectId);
    validateSprintId(sprintId);
    validateProjectIdAndSprintId(projectId, sprintId);

    Sprint sprint = sprintService.findById(sprintId);

    if(!sprint.getStatus().equals(SprintStatus.TODO.toString())) {
      log.error("(startSprint)invalid sprint status: {}", sprint.getStatus());
      throw new SprintStatusTodoInvalidException();
    }

    sprint.setStatus(SprintStatus.START.toString());
    if (title != null && !title.isEmpty()) {
      sprint.setTitle(title);
    }

    if(LocalDate.now().isBefore(LocalDate.parse(startDate)) || LocalDate.now().isAfter(LocalDate.parse(startDate))){
      sprint.setStartDate(LocalDate.now());
    } else {
      sprint.setStartDate(LocalDate.parse(startDate));
    }

    sprint.setEndDate(LocalDate.parse(endDate));
    sprintService.save(sprint);

    var notification = new ActivityLog();
    notification.setAction(START_SPRINT);
    notification.setUserId(userId);
    notification.setSprintId(sprint.getId());
    activityLogService.create(notification);

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

    validateProjectId(projectId);
    validateSprintId(sprintId);
    validateProjectIdAndSprintId(projectId, sprintId);

    Sprint sprint = sprintService.findById(sprintId);

    if (title != null && !title.isEmpty()) {
      sprint.setTitle(title);
    }

    if(sprint.getStatus().equals(SprintStatus.TODO.toString())){
      if(LocalDate.now().isBefore(LocalDate.parse(startDate))){
        sprint.setStartDate(LocalDate.parse(startDate));
      } else {
        sprint.setStartDate(LocalDate.now().plusDays(2));
      }
    } else {
      sprint.setStartDate(LocalDate.parse(startDate));
    }

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
    validateProjectId(projectId);
    validateSprintId(id);
    validateProjectIdAndSprintId(projectId, id);
    Sprint sprint = sprintService.findById(id);

    log.info("(getSprint)sprint: {}", sprint);
    return sprintMapper.toSprintResponse(sprint);
  }

  @Override
  public ProgressStatisticsResponse getProgressStatistics(String projectId, String sprintId) {
    log.info("(getProgressStatistics) projectId: {}, sprintId {}", projectId, sprintId);
    validateProjectId(projectId);
    validateSprintId(sprintId);
    validateProjectIdAndSprintId(projectId, sprintId);
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
  public void deleteSprint(String userId, String projectId, String id) {
    log.info("(deleteSprint) projectId: {}, sprintId {}", projectId, id);

    validateProjectId(projectId);
    validateSprintId(id);
    validateProjectIdAndSprintId(projectId, id);

    sprintProgressService.deleteAllBySprintId(id);
    List<Task> tasks = taskService.getAllBySprintId(id);
    for (Task task : tasks) {
      taskService.updateStatus(task.getId(), TODO.toString(), userId);
      taskService.updateSprintId(projectId,task.getId(), null, userId);
    }
    sprintService.deleteById(id);

    var notification = new ActivityLog();
    notification.setAction(DELETE_SPRINT);
    notification.setUserId(userId);
    notification.setSprintId(id);
    activityLogService.create(notification);
  }

  @Override
  public CompleteSprintResponse completeSprint(String projectId, String sprintId) {
    log.info("(completeSprint)");
    validateProjectId(projectId);
    validateSprintId(sprintId);
    validateProjectIdAndSprintId(projectId, sprintId);

    int countTaskCompleted = taskService.countBySprintIdAndProjectIdAndStatusDone(sprintId, projectId);
    int countTaskFailed =taskService.countBySprintIdAndProjectIdAndStatusNotDone(sprintId, projectId);
    return CompleteSprintResponse.from(countTaskCompleted, countTaskFailed);
  }

  @Override
  public void confirmCompleteSprint(String userId, String projectId, String sprintId) {
    log.info("(confirmCompleteSprint)projectId: {}, sprintId {}", projectId, sprintId);
    validateProjectId(projectId);
    validateSprintId(sprintId);
    validateProjectIdAndSprintId(projectId, sprintId);
    List<Task> tasks = taskService.findAllByProjectIdAndSprintIdAndStatusNotDone(projectId, sprintId);
    List<Task> saveTakes = new ArrayList<>();
    for (var task : tasks) {
      task.setStatus(TODO.toString());
      task.setSprintId(null);
      task.setStartDate(null);
      task.setDueDate(null);
      saveTakes.add(task);
    }
    taskService.saveAll(saveTakes);
    sprintService.updateSprintComplete(sprintId);

    var notification = new ActivityLog();
    notification.setAction(COMPLETE_SPRINT);
    notification.setUserId(userId);
    notification.setSprintId(sprintId);
    activityLogService.create(notification);

  }

  private boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
    return !startDate.isBefore(endDate);
  }

  private void validateProjectId(String projectId){
    log.info("(validateProjectId)projectId: {}", projectId);
    if(!projectService.existById(projectId)){
      log.error("(validateProjectId) project not found: projectId {}", projectId);
      throw new ProjectNotFoundException();
    }
  }

  private void validateSprintId(String sprintId){
    log.info("(validateSprintId)sprintId: {}", sprintId);
    if(!sprintService.existById(sprintId)){
      log.error("(validateSprintId) sprintId not found: sprintId {}", sprintId);
      throw new SprintNotFoundException();
    }
  }

  private void validateProjectIdAndSprintId(String projectId, String sprintId) {
    log.info("(validateProjectIdAndSprintId)projectId: {}, sprintId: {}", projectId, sprintId);
    Sprint sprint = sprintService.findById(sprintId);
    if (!sprint.getProjectId().equals(projectId)) {
      log.error("(validateProjectIdAndSprintId)sprintId {} not part of projectId {}", sprintId, projectId);
      throw new SprintNotExistProjectException();
    }
  }
}
