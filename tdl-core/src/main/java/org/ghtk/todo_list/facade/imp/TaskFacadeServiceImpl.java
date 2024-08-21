package org.ghtk.todo_list.facade.imp;

import static org.ghtk.todo_list.constant.ActivityLogConstant.AssigneeAction.ADD_ASSIGNEE;
import static org.ghtk.todo_list.constant.ActivityLogConstant.TaskAction.*;
import static org.ghtk.todo_list.constant.CacheConstant.UPDATE_STATUS_TASK;
import static org.ghtk.todo_list.constant.CacheConstant.UPDATE_STATUS_TASK_KEY;
import static org.ghtk.todo_list.constant.TaskStatus.DONE;
import static org.ghtk.todo_list.constant.TaskStatus.IN_PROGRESS;
import static org.ghtk.todo_list.constant.TaskStatus.TODO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.ImageConstant;
import org.ghtk.todo_list.constant.SprintStatus;
import org.ghtk.todo_list.constant.TaskStatus;
import org.ghtk.todo_list.dto.response.UserResponse;
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.entity.TaskAssignees;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.entity.Type;
import org.ghtk.todo_list.exception.ApproachEndDateSprintException;
import org.ghtk.todo_list.exception.DueDateTaskInvalidSprintEndDateException;
import org.ghtk.todo_list.exception.DueDateTaskInvalidStartDateException;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.SprintDoneException;
import org.ghtk.todo_list.exception.SprintNotFoundException;
import org.ghtk.todo_list.exception.SprintNotStartException;
import org.ghtk.todo_list.exception.StatusTaskInvalidException;
import org.ghtk.todo_list.exception.TaskAssignmentExistsException;
import org.ghtk.todo_list.exception.TaskHasNotStartedException;
import org.ghtk.todo_list.exception.TaskNotExistUserException;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.exception.UserNotFoundException;
import org.ghtk.todo_list.facade.TaskFacadeService;
import org.ghtk.todo_list.model.response.LabelResponse;
import org.ghtk.todo_list.model.response.SprintDetailResponse;
import org.ghtk.todo_list.model.response.SprintsProjectDetailResponse;
import org.ghtk.todo_list.mapper.TaskMapper;
import org.ghtk.todo_list.model.response.TaskDetailResponse;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.model.response.TypeResponse;
import org.ghtk.todo_list.model.response.UpdateDueDateTaskResponse;
import org.ghtk.todo_list.service.ActivityLogService;
import org.ghtk.todo_list.service.AuthAccountService;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.CommentService;
import org.ghtk.todo_list.service.LabelAttachedService;
import org.ghtk.todo_list.service.LabelService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.ProjectUserService;
import org.ghtk.todo_list.service.RedisCacheService;
import org.ghtk.todo_list.service.SprintProgressService;
import org.ghtk.todo_list.service.SprintService;
import org.ghtk.todo_list.service.TaskAssigneesService;
import org.ghtk.todo_list.service.TaskService;
import org.ghtk.todo_list.service.TypeService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskFacadeServiceImpl implements TaskFacadeService {

  private final ProjectService projectService;
  private final TaskService taskService;
  private final AuthUserService authUserService;
  private final AuthAccountService authAccountService;
  private final SprintService sprintService;
  private final TypeService typeService;
  private final LabelService labelService;
  private final TaskAssigneesService taskAssigneesService;
  private final ProjectUserService projectUserService;
  private final RedisCacheService redisCacheService;
  private final TaskMapper taskMapper;
  private final SprintProgressService sprintProgressService;
  private final CommentService commentService;
  private final LabelAttachedService labelAttachedService;
  private final ActivityLogService activityLogService;

  @Override
  public List<TaskResponse> getAllTaskByProjectParticipant(String userId) {
    log.info("(getAllTaskByProjectParticipant)userId: {}", userId);

    List<Project> projects = projectService.getAllProject(userId);
    List<TaskResponse> taskResponseList = new ArrayList<>();

    for (Project project : projects) {
      taskResponseList.addAll(taskService.getAllTasksByProjectId(project.getId()));
    }

    for (TaskResponse taskResponse : taskResponseList) {
      taskResponse.setUserId(taskAssigneesService.findUserIdByTaskId(taskResponse.getId()));
    }
    return taskResponseList;
  }

  @Override
  public List<TaskDetailResponse> getAllTaskByProjectId(String userId, String projectId) {
    log.info("(getAllTaskByProjectId)projectId: {}", projectId);
    validateProjectId(projectId);
    List<TaskDetailResponse> taskDetailResponseList = taskService.getAllTaskDetailByProjectId(
        projectId);
    for (TaskDetailResponse taskDetailResponse : taskDetailResponseList) {
      log.info("(getAllTaskByProjectId)taskDetailResponse: {}", taskDetailResponse);
      fillData(taskDetailResponse);
    }
    return taskDetailResponseList;
  }

  @Override
  public TaskDetailResponse getTaskByTaskId(String userId, String projectId, String taskId) {
    log.info("(getTaskByTaskId)taskId: {},projectId: {}", taskId, projectId);
    validateProjectId(projectId);
    TaskDetailResponse taskDetailResponse = taskService.findById(taskId,
        taskAssigneesService.findUserIdByTaskId(taskId));
    fillData(taskDetailResponse);
    return taskDetailResponse;
  }

  @Override
  public TaskResponse updateStatusTask(String userId, String projectId, String taskId,
      String status) {
    log.info("(updateStatusTask)taskId: {},projectId: {}", taskId, projectId);
    validateProjectId(projectId);
    if (!TaskStatus.isValid(status)) {
      log.error("(updateStatusTask)taskId: {},projectId: {}", taskId, projectId);
      throw new StatusTaskInvalidException();
    }

    if (taskService.findById(taskId).getStatus().equals(TODO.toString()) && !status.toUpperCase()
        .equals(IN_PROGRESS.toString())) {
      log.error("(updateStatusTask)taskId: {} hasn't started", taskId);
      throw new TaskHasNotStartedException();
    }

    if (status.toUpperCase().equals(TaskStatus.DONE.toString())) {
      log.info("(updateStatusTask)status: {}", status);
      sprintProgressService.updateCompleteTask(taskId);
    }

    var notification = new ActivityLog();
    notification.setAction(UPDATE_STATUS_TASK);
    notification.setUserId(userId);
    notification.setTaskId(taskId);
    activityLogService.create(notification);

    return taskService.updateStatus(taskId, status.toUpperCase(),
        taskAssigneesService.findUserIdByTaskId(taskId));
  }

  @Override
  @Transactional
  public TaskResponse updatePointTask(String userId, String projectId, String taskId, int point) {
    log.info("(updatePointTask)taskId: {},projectId: {}", taskId, projectId);
    validateProjectId(projectId);
    return taskService.updatePoint(taskId, point, userId);
  }

  @Override
  public TaskResponse updateSprintTask(String userId, String projectId, String sprintId,
      String taskId) {
    log.info("(updateSprintTask)sprintId: {}, taskId: {},projectId: {}", sprintId, taskId,
        projectId);
    validateProjectId(projectId);
    validateTaskId(taskId);
    var notification = new ActivityLog();
    notification.setAction(UPDATE_SPRINT_TASK);
    notification.setUserId(userId);
    notification.setTaskId(taskId);
    activityLogService.create(notification);

    var task = taskService.findById(taskId);
    if (task.getSprintId() == null && sprintId != null) {
      validateSprintId(sprintId);
      log.info("(updateSprintTask)task not in sprintId ");
      var sprintProgress = sprintProgressService.findBySprintId(sprintId);
      sprintProgress.setTotalTask(sprintProgress.getTotalTask() + 1);
      if (task.getStatus().equals(DONE.toString())) {
        sprintProgress.setCompleteTask(sprintProgress.getCompleteTask() - 1);
      }
      sprintProgressService.save(sprintProgress);
      taskService.updateStatus(taskId, TODO.toString(), userId);
      return taskService.updateSprintId(projectId, taskId, sprintId,
          taskAssigneesService.findUserIdByTaskId(taskId));
    } else if (task.getSprintId() != null && sprintId != null) {
      validateSprintId(sprintId);
      log.info("(updateSprintTask)task allocated to another sprint");
      var sprintProgress = sprintProgressService.findBySprintId(task.getSprintId());
      sprintProgress.setTotalTask(sprintProgress.getTotalTask() - 1);
      if (task.getStatus().equals(DONE.toString())) {
        sprintProgress.setCompleteTask(sprintProgress.getCompleteTask() - 1);
      }
      sprintProgressService.save(sprintProgress);
      taskService.updateStatus(taskId, TODO.toString(), userId);
      var sprintProgress1 = sprintProgressService.findBySprintId(sprintId);
      sprintProgress1.setTotalTask(sprintProgress1.getTotalTask() + 1);
      sprintProgressService.save(sprintProgress1);
      return taskService.updateSprintId(projectId, taskId, sprintId,
          taskAssigneesService.findUserIdByTaskId(taskId));
    } else if (task.getSprintId() != null) {
      log.info("(updateSprintTask)task out sprint");
      var sprintProgress = sprintProgressService.findBySprintId(task.getSprintId());
      sprintProgress.setTotalTask(sprintProgress.getTotalTask() - 1);
      if (task.getStatus().equals(DONE.toString())) {
        sprintProgress.setCompleteTask(sprintProgress.getCompleteTask() - 1);
      }
      sprintProgressService.save(sprintProgress);
      taskService.updateStatus(taskId, TODO.toString(), userId);
      return taskService.updateSprintId(projectId, taskId, null,
          taskAssigneesService.findUserIdByTaskId(taskId));
    } else {
      log.info("(updateSprintTask)move position");
      return taskService.updateSprintId(projectId, taskId, null,
          taskAssigneesService.findUserIdByTaskId(taskId));
    }

  }

  void validateProjectId(String projectId) {
    log.info("(validateProjectId)projectId: {}", projectId);
    if (!projectService.existById(projectId)) {
      log.error("(validateProjectId)projectId: {}", projectId);
      throw new ProjectNotFoundException();
    }
  }

  void validateSprintId(String sprintId) {
    log.info("(validateSprintId)sprintId: {}", sprintId);
    if (!sprintService.existById(sprintId)) {
      log.error("(validateSprintId)sprintId: {}", sprintId);
      throw new SprintNotFoundException();
    }
  }

  @Override
  public TaskAssignees agileTaskByUser(String userId, String taskId) {
    log.info("(agileTaskByUser) userId: {}, taskId: {}", userId, taskId);
    if (taskAssigneesService.existsByUserIdAndTaskId(userId, taskId)) {
      log.error("(agileTaskByUser)Task with Id {} is already assigned to user with Id {}", taskId,
          userId);
      throw new TaskAssignmentExistsException();
    }
    if (!taskService.existsByUserIdAndTaskId(userId, taskId)) {
      log.error("(agileTaskByUser)Task with Id {} does not exist for user with Id {}", taskId,
          userId);
      throw new TaskNotExistUserException();
    }
    TaskAssignees taskAssignees = taskAssigneesService.findByTaskId(taskId);
    log.info("(agileTaskByUser) TaskAssignees: {}", taskAssignees);
    if (taskAssignees == null) {
      log.warn("(agileTaskByUser) is null");
      taskAssignees = new TaskAssignees();
    }

    taskAssignees.setUserId(userId);
    taskAssignees.setTaskId(taskId);

    var notification = new ActivityLog();
    notification.setAction(ADD_ASSIGNEE);
    notification.setUserId(userId);
    notification.setTaskId(taskId);
    activityLogService.create(notification);

    return taskAssigneesService.save(taskAssignees);
  }

  @Override
  public TaskResponse cloneTask(String userId, String projectId, String taskId) {
    log.info("(cloneTask)taskId: {},projectId: {}", taskId, projectId);
    validateProjectId(projectId);
    var user = authUserService.findByUnassigned();
    var task = taskService.findById(taskId);

    var clonedTask = new Task();
    clonedTask.setTitle(task.getTitle());
    clonedTask.setStatus(TODO.toString());
    clonedTask.setUserId(userId);
    clonedTask.setChecklist(task.getChecklist());
    clonedTask.setDescription(task.getDescription());
    clonedTask.setProjectId(task.getProjectId());
    var taskClone = taskService.save(clonedTask);
    agileTaskByUser(user.getId(), taskClone.getId());

    var notification = new ActivityLog();
    notification.setAction(CLONE_TASK);
    notification.setUserId(userId);
    notification.setTaskId(taskClone.getId());
    activityLogService.create(notification);

    return TaskResponse.of(
        taskClone.getId(),
        taskClone.getTitle(),
        0,
        taskClone.getStatus(),
        taskClone.getKeyProjectTask(),
        user.getId());
  }

  @Override
  public UpdateDueDateTaskResponse updateStartDateDueDateTask(String userId, String projectId,
      String sprintId, String taskId, String dueDate) {
    log.info("(updateStartDateDueDateTask)projectId: {}, sprintId: {}, taskId: {}", projectId,
        sprintId, taskId);

    validateUserId(userId);
    validateProjectId(projectId);
    validateSprintId(sprintId);

    Sprint sprint = sprintService.findById(sprintId);

    if (sprint.getStatus().equals(SprintStatus.COMPLETE.toString())) {
      log.error("(updateStartDateDueDateTask)sprintId: {} done", sprintId);
      throw new SprintDoneException();
    }

    if (!sprint.getStatus().equals(SprintStatus.START.toString())) {
      log.error("(updateStartDateDueDateTask)sprintId: {} not start", sprintId);
      throw new SprintNotStartException();
    }

    validateTaskId(taskId);
    validateDueDateTask(sprint, dueDate);

    var notification = new ActivityLog();
    notification.setAction(taskId + UPDATE_STATUS_TASK_KEY);
    notification.setUserId(userId);
    notification.setTaskId(taskId);
    activityLogService.create(notification);

    return taskService.updateDueDate(projectId, sprintId, taskId, dueDate);
  }

  @Override
  public List<TaskResponse> getAllBySprintId(String projectId, String sprintId) {
    log.info("(getAllBySprintId)sprintId: {}", sprintId);
    validateProjectId(projectId);
    List<TaskResponse> responses = new ArrayList<>();
    if (sprintService.existById(sprintId)) {
      log.info("(allBySprintId)sprintId: {}", sprintId);
      var tasks = taskService.getAllBySprintId(sprintId);
      for (Task task : tasks) {
        TaskResponse taskResponse = TaskResponse.of(task.getId(), task.getTitle(), task.getPoint(),
            task.getStatus(), task.getKeyProjectTask(),
            taskAssigneesService.findUserIdByTaskId(task.getId()));
        responses.add(taskResponse);
      }
      return responses;
    } else {
      log.info("(allBySprintId)sprintId: {} don't exist", sprintId);
      return responses;
    }
  }

  @Override
  public TaskDetailResponse createTask(String userId, String projectId, String title) {
    log.info("(createTask)userId: {},projectId: {}", userId, projectId);
    validateProjectId(projectId);

    Task task = new Task();
    task.setTitle(title);
    task.setUserId(userId);
    task.setProjectId(projectId);
    task.setStatus(TODO.toString());
    task.setKeyProjectTask(generateKeyProjectTask(projectId));

    Type type = typeService.findByProjectIdAndTitle(projectId, ImageConstant.STORY);
    task.setTypeId(type.getId());
    Task savedTask = taskService.save(task);

    var user = authUserService.findByUnassigned();
    agileTaskByUser(user.getId(), savedTask.getId());

    var notification = new ActivityLog();
    notification.setAction(CREATE_TASK);
    notification.setUserId(userId);
    notification.setTaskId(savedTask.getId());
    activityLogService.create(notification);

    TaskDetailResponse taskDetailResponse = TaskDetailResponse.builder()
        .id(savedTask.getId())
        .title(savedTask.getTitle())
        .point(savedTask.getPoint())
        .status(savedTask.getStatus())
        .keyProjectTask(savedTask.getKeyProjectTask())
        .userResponse(new UserResponse(user.getId(), null, user.getFirstName(), user.getMiddleName(),
            user.getLastName(), user.getEmail(), null))
        .sprintDetailResponse(new SprintDetailResponse())
        .typeResponse(
            TypeResponse.of(type.getId(), type.getTitle(), type.getImage(), type.getDescription()))
        .labelResponseList(null)
        .build();

    fillData(taskDetailResponse);
    return taskDetailResponse;
  }

  @Override
  @Transactional
  public void deleteTask(String userId, String projectId, String taskId) {
    log.info("(deleteTask)projectId: {}, taskId: {}", projectId, taskId);
    validateProjectId(projectId);
    validateTaskId(taskId);

    taskAssigneesService.deleteAllByTaskId(taskId);
    commentService.deleteAllCommentByTaskId(taskId);
    labelAttachedService.deleteAllByTaskId(taskId);
    activityLogService.deleteAllByTaskId(taskId);
    taskService.deleteTask(userId, projectId, taskId);

    var notification = new ActivityLog();
    notification.setAction(DELETE_TASK);
    notification.setUserId(userId);
    activityLogService.create(notification);
  }

  @Override
  public TaskResponse updateTitleTask(String userId, String projectId, String taskId,
      String title) {
    log.info("(updateTitleTask)userId: {}, projectId: {}, taskId: {}", userId, projectId, taskId);
    validateUserId(userId);
    validateProjectId(projectId);
    validateTaskId(taskId);
    validateProjectIdAndTaskId(projectId, taskId);
    taskService.updateTitle(taskId, title);

    var notification = new ActivityLog();
    notification.setAction(UPDATE_TASK);
    notification.setUserId(userId);
    notification.setTaskId(taskId);
    activityLogService.create(notification);

    return TaskResponse.from(taskService.findById(taskId),
        taskAssigneesService.findUserIdByTaskId(taskId));
  }

  @Override
  public List<TaskResponse> getAllTaskAssigneesForUser(String userId) {
    log.info("(getAllTaskAssigneesForUser)userId: {}", userId);
    return taskMapper.toTaskResponses(taskService.getAllTaskAssigneesForUser(userId));
  }

  @Override
  public List<TaskDetailResponse> searchTask(String searchValue, String typeId, String labelId,
      String status, String assignee, String userId, String projectId, String sprintId) {
    log.info("(searchTask)searchValue: {}, userId: {}, projectId: {}", searchValue, userId,
        projectId);

    var taskSearch = taskService.searchTask(searchValue, typeId, labelId, status, assignee,
        userId, projectId, sprintId);
    List<TaskDetailResponse> responses = new ArrayList<>();
    for (var task : taskSearch) {
      TaskDetailResponse response = new TaskDetailResponse();
      response.setUserResponse(new UserResponse());
      response.setTypeResponse(new TypeResponse());
      response.setSprintDetailResponse(new SprintDetailResponse());

      response.setId(task.getId());
      response.setTitle(task.getTitle());
      response.setPoint(task.getPoint());
      response.setStatus(task.getStatus());
      response.setKeyProjectTask(task.getKeyProjectTask());
      response.getUserResponse().setId(taskAssigneesService.findUserIdByTaskId(task.getId()));
      response.getSprintDetailResponse().setSprintId(task.getSprintId());
      response.getTypeResponse().setId(task.getTypeId());
      fillData(response);
      responses.add(response);
    }
    return responses;
  }

  @Override
  public List<SprintsProjectDetailResponse> getAllTaskByAllSprint(String projectId) {
    log.info("(getAllTaskByAllSprint)projectId: {}", projectId);
    validateProjectId(projectId);
    List<Sprint> sprintList = sprintService.findSprintsByProjectIdAndStatus(projectId,
        SprintStatus.START.toString());
    List<SprintsProjectDetailResponse> sprintsProjectDetailResponseList = new ArrayList<>();
    for (Sprint sprint : sprintList) {
      SprintsProjectDetailResponse sprintsProjectDetailResponse = new SprintsProjectDetailResponse();
      sprintsProjectDetailResponse.setId(sprint.getId());
      sprintsProjectDetailResponse.setTitle(sprint.getTitle());
      sprintsProjectDetailResponse.setStartDate(sprint.getStartDate());
      sprintsProjectDetailResponse.setEndDate(sprint.getEndDate());
      List<Task> taskList = taskService.getAllBySprintId(sprint.getId());
      sprintsProjectDetailResponse.setTaskResponseList(taskMapper.toTaskResponses(taskList));
      sprintsProjectDetailResponseList.add(sprintsProjectDetailResponse);
    }

    return sprintsProjectDetailResponseList;
  }

  void validateUserId(String userId) {
    log.info("(validateUserId)userId: {}", userId);
    if (!authUserService.existById(userId)) {
      log.error("(validateUserId)userId: {} not found", userId);
      throw new UserNotFoundException();
    }
  }

  void validateTaskId(String taskId) {
    log.info("(validateTaskId)taskId: {}", taskId);
    if (!taskService.existById(taskId)) {
      log.error("(validateTaskId)taskId: {} not found", taskId);
      throw new TaskNotFoundException();
    }
  }

  void validateDueDateTask(Sprint sprint, String dueDate) {
    log.info("(validateDueDateTask)sprint: {}, dueDate: {}", sprint, dueDate);

    if (!LocalDate.now().isBefore(LocalDate.parse(dueDate))) {
      log.error("(validateDueDateTask)dueDate: {} invalid", dueDate);
      throw new DueDateTaskInvalidStartDateException();
    }
    if (sprint.getEndDate().equals(LocalDate.now())) {
      log.error("(validateDueDateTask)dueDate: {} invalid", dueDate);
      throw new ApproachEndDateSprintException();
    }
    if (sprint.getEndDate().isBefore(LocalDate.parse(dueDate))) {
      log.error("(validateDueDateTask)dueDate: {} invalid", dueDate);
      throw new DueDateTaskInvalidSprintEndDateException();
    }
  }

  @Override
  public List<TaskDetailResponse> getAllTaskByProjectIdAndStatus(String userId, String projectId,
      String status) {
    log.info("(getAllTaskByProjectIdAndStatus)projectId: {}, status: {}", projectId, status);
    String statusFormat = status.trim().toUpperCase();
    if (!TaskStatus.isValid(statusFormat)) {
      log.error("(getAllTaskByProjectIdAndStatus) status task not found: status {}", status);
      throw new StatusTaskInvalidException();
    }
    validateProjectId(projectId);

    List<TaskDetailResponse> taskDetailResponseList = taskMapper.toTaskDetailResponses(
        taskService.getAllTasksByProjectIdAndStatus(projectId, statusFormat));
    for (TaskDetailResponse taskDetailResponse : taskDetailResponseList) {
      log.info("(getAllTaskByProjectIdAndStatus)taskDetailResponse: {}", taskDetailResponse);
      fillData(taskDetailResponse);
    }
    return taskDetailResponseList;
  }

  void validateProjectIdAndTaskId(String projectId, String taskId) {
    log.info("(validateProjectIdAndTaskId)projectId: {}, taskId: {}", projectId, taskId);
    if (!taskService.existByProjectIdAndTaskId(projectId, taskId)) {
      log.error("(validateProjectIdAndTaskId)taskId: {} not found", taskId);
      throw new TaskNotFoundException();
    }
  }

  private String generateKeyProjectTask(String projectId) {
    Project project = projectService.getProjectById(projectId);
    int counterTask = project.getCounterTask();
    project.setCounterTask(++counterTask);
    projectService.updateProject(project);
    return project.getKeyProject() + "-" + project.getCounterTask();
  }

  private void fillData(TaskDetailResponse taskDetailResponse) {
    log.info("(fillData)taskDetailResponse: {}", taskDetailResponse);
    UserResponse userResponse = authUserService.getUserResponseById(taskAssigneesService.findUserIdByTaskId(taskDetailResponse.getId()));
    if(!userResponse.getId().equals(authUserService.findByUnassigned().getId())) {
      userResponse.setUsername(authAccountService.findByUserIdWithThrow(userResponse.getId()).getUsername());
    }
    taskDetailResponse.setUserResponse(userResponse);
    if (taskDetailResponse.getSprintDetailResponse().getSprintId() != null) {
      Sprint sprint = sprintService.findById(
          taskDetailResponse.getSprintDetailResponse().getSprintId());
      taskDetailResponse.getSprintDetailResponse().setSprintTitle(sprint.getTitle());
      taskDetailResponse.getSprintDetailResponse().setSprintStatus(sprint.getStatus());
    }

    if (taskDetailResponse.getTypeResponse().getId() != null) {
      Type type = typeService.findById(taskDetailResponse.getTypeResponse().getId());
      taskDetailResponse.getTypeResponse().setTitle(type.getTitle());
      taskDetailResponse.getTypeResponse().setImage(type.getImage());
      taskDetailResponse.getTypeResponse().setDescription(type.getDescription());
    }

    List<LabelResponse> labelResponseList = labelService.getAllLabelByTask(
        taskDetailResponse.getId());
    taskDetailResponse.setLabelResponseList(labelResponseList);
  }
}
