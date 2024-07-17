package org.ghtk.todo_list.facade.imp;

import static org.ghtk.todo_list.constant.CacheConstant.UPDATE_STATUS_TASK;
import static org.ghtk.todo_list.constant.CacheConstant.UPDATE_STATUS_TASK_KEY;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.CacheConstant;
import org.ghtk.todo_list.constant.RoleProjectUser;
import org.ghtk.todo_list.constant.TaskStatus;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.entity.TaskAssignees;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.exception.DueDateTaskInvalidSprintEndDateException;
import org.ghtk.todo_list.exception.DueDateTaskInvalidStartDateException;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.RoleProjectNotAllowException;
import org.ghtk.todo_list.exception.SprintNotFoundException;
import org.ghtk.todo_list.exception.StatusTaskInvalidException;
import org.ghtk.todo_list.exception.TaskAssignmentExistsException;
import org.ghtk.todo_list.exception.TaskNotExistUserException;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.exception.StatusTaskKeyNotFoundException;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.exception.UserNotFoundException;
import org.ghtk.todo_list.facade.TaskFacadeService;
import org.ghtk.todo_list.model.response.StartSprintResponse;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.model.response.UpdateDueDateTaskResponse;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.ProjectUserService;
import org.ghtk.todo_list.service.RedisCacheService;
import org.ghtk.todo_list.service.SprintService;
import org.ghtk.todo_list.service.TaskAssigneesService;
import org.ghtk.todo_list.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskFacadeServiceImpl implements TaskFacadeService {

  private final ProjectService projectService;
  private final TaskService taskService;
  private final AuthUserService authUserService;
  private final SprintService sprintService;
  private final TaskAssigneesService taskAssigneesService;
  private final ProjectUserService projectUserService;
  private final RedisCacheService redisCacheService;

  @Override
  public List<TaskResponse> getAllTaskByProjectId(String userId, String projectId) {
    log.info("(getAllTaskByProjectId)projectId: {}", projectId);
    validateProjectId(projectId);
    return taskService.getAllTasksByProjectId(projectId);
  }

  @Override
  public TaskResponse getTaskByTaskId(String userId, String projectId, String taskId) {
    log.info("(getTaskByTaskId)taskId: {},projectId: {}", taskId, projectId);
    validateProjectId(projectId);
    return taskService.findById(taskId, taskAssigneesService.findUserIdByTaskId(taskId));
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
    String statusTaskKey = taskId + UPDATE_STATUS_TASK_KEY;
    redisCacheService.save(UPDATE_STATUS_TASK, taskId, statusTaskKey);
    return taskService.updateStatus(taskId, status.toUpperCase(), taskAssigneesService.findUserIdByTaskId(taskId));
  }

  @Override
  public TaskResponse updateSprintTask(String userId, String projectId, String sprintId, String taskId) {
    log.info("(updateSprintTask)sprintId: {}, taskId: {},projectId: {}", sprintId, taskId, projectId);
    validateProjectId(projectId);
    validateSprintId(sprintId);
    return taskService.updateSprintId(projectId, taskId, sprintId, taskAssigneesService.findUserIdByTaskId(taskId));
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
    if(taskAssigneesService.existsByUserIdAndTaskId(userId, taskId)) {
      log.error("(agileTaskByUser)Task with Id {} is already assigned to user with Id {}", taskId, userId);
      throw new TaskAssignmentExistsException();
    }
    if(!taskService.existsByUserIdAndTaskId(userId, taskId)){
      log.error("(agileTaskByUser)Task with Id {} does not exist for user with Id {}", taskId, userId);
      throw new TaskNotExistUserException();
    }
    TaskAssignees taskAssignees = new TaskAssignees();
    taskAssignees.setUserId(userId);
    taskAssignees.setTaskId(taskId);

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
    clonedTask.setStatus(TaskStatus.TODO.toString());
    clonedTask.setUserId(userId);
    clonedTask.setChecklist(task.getChecklist());
    clonedTask.setDescription(task.getDescription());
    clonedTask.setLabel(task.getLabel());
    clonedTask.setProjectId(task.getProjectId());
    var taskClone = taskService.save(clonedTask);
    agileTaskByUser(user.getId(), taskClone.getId());
    return TaskResponse.of(
        taskClone.getId(),
        taskClone.getTitle(),
        0,
        taskClone.getStatus(),
        user.getId());
  }

  @Override
  public UpdateDueDateTaskResponse updateStartDateDueDateTask(String userId, String projectId,
      String sprintId, String taskId, String statusTaskKey, String dueDate) {
    log.info("(updateStartDateDueDateTask)projectId: {}, sprintId: {}, taskId: {}", projectId,
        sprintId, taskId);

    var redisStatusTaskKey = redisCacheService.get(UPDATE_STATUS_TASK, taskId);
    if (redisStatusTaskKey.isEmpty()) {
      log.error("(updateStartDateDueDateTask)statusTaskKey: {} not found", statusTaskKey);
      throw new StatusTaskKeyNotFoundException();
    }

    validateUserId(userId);
    String roleProjectUser = projectUserService.getRoleProjectUser(userId, projectId);
    if (roleProjectUser.equals(RoleProjectUser.VIEWER.toString())) {
      log.error("(updateStartDateDueDateTask)role: {} not allowed", roleProjectUser);
      throw new RoleProjectNotAllowException();
    }

    validateProjectId(projectId);
    validateSprintId(sprintId);
    validateDueDateTask(projectId, sprintId, dueDate);
    validateTaskId(taskId);

    return taskService.updateDueDate(projectId, sprintId, taskId, dueDate);
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

  void validateDueDateTask(String projectId, String sprintId, String dueDate) {
    log.info("(validateDueDateTask)projectId: {}, sprintId: {}", projectId, sprintId);
    Sprint sprint = sprintService.findSprintByProjectIdAndSprintId(projectId, sprintId);

    if(!LocalDate.now().isBefore(LocalDate.parse(dueDate))) {
      log.error("(validateDueDateTask)dueDate: {} invalid", dueDate);
      throw new DueDateTaskInvalidStartDateException();
    }
    if (sprint.getEndDate().isBefore(LocalDate.parse(dueDate))) {
      log.error("(validateDueDateTask)dueDate: {} invalid", dueDate);
      throw new DueDateTaskInvalidSprintEndDateException();
    }
  }
}
