package org.ghtk.todo_list.service.impl;

import java.time.LocalDate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.TaskStatus;
import org.ghtk.todo_list.dto.response.UserResponse;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.filter.FilterTask;
import org.ghtk.todo_list.model.response.SprintDetailResponse;
import org.ghtk.todo_list.model.response.TaskDetailResponse;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.model.response.TypeResponse;
import org.ghtk.todo_list.model.response.UpdateDueDateTaskResponse;
import org.ghtk.todo_list.repository.TaskRepository;
import org.ghtk.todo_list.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImp implements TaskService {

  private final TaskRepository taskRepository;

  @Override
  public List<TaskResponse> getAllTasksByProjectId(String projectId) {
    log.info("(getAllTasksByProjectId)projectId: {}", projectId);
    List<Task> tasks = taskRepository.getAllTasksByProjectId(projectId);
    return tasks.stream()
        .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.getPoint(),
            task.getStatus(), task.getKeyProjectTask()))
        .collect(Collectors.toList());
  }

  @Override
  public List<TaskDetailResponse> getAllTaskDetailByProjectId(String projectId) {
    log.info("(getAllTaskDetailByProjectId)projectId: {}", projectId);
    List<Task> tasks = taskRepository.getAllTasksByProjectId(projectId);
    return tasks.stream()
        .map(task -> TaskDetailResponse.of(task.getId(), task.getTitle(), task.getPoint(),
            task.getStatus(), task.getKeyProjectTask(),
            null,
            SprintDetailResponse.of(task.getSprintId(), null, null),
            TypeResponse.of(task.getTypeId(), null, null, null),
            null))
        .collect(Collectors.toList());
  }

  @Override
  public List<Task> getAllTasksByProjectIdAndStatus(String projectId, String status) {
    log.info("(getAllTasksByProjectIdAndStatus)");
    return taskRepository.getAllTasksByProjectIdAndStatus(projectId, status);
  }

  @Override
  public TaskDetailResponse findById(String taskId, String userId) {
    log.info("(getTaskByTaskId)projectId: {}", taskId);
    var task = taskRepository.findById(taskId).orElseThrow(() -> {
      throw new TaskNotFoundException();
    });

    return TaskDetailResponse.of(task.getId(), task.getTitle(), task.getPoint(), task.getStatus(),
        task.getKeyProjectTask(), new UserResponse(userId, null, null, null, null, null, null),
        SprintDetailResponse.of(task.getSprintId(), null, null),
        TypeResponse.of(task.getTypeId(), null, null, null), null);
  }

  @Override
  public TaskResponse updateStatus(String taskId, String taskStatus, String userId) {
    log.info("(updateStatus)taskId: {}, status: {}", taskId, taskStatus);
    var task = taskRepository
        .findById(taskId)
        .orElseThrow(() -> {
          log.error("(updateStatus)taskId: {}, status: {}", taskId, taskStatus);
          throw new TaskNotFoundException();
        });
    task.setStatus(taskStatus);
    taskRepository.save(task);
    return TaskResponse.of(task.getId(), task.getTitle(), task.getPoint(), task.getStatus(),
        task.getKeyProjectTask(), userId);
  }

  @Override
  @Transactional
  public TaskResponse updatePoint(String taskId, int point, String userId) {
    log.info("(updatePoint)taskId: {}, userId: {}", taskId, userId);
    var task = taskRepository
        .findById(taskId)
        .orElseThrow(() -> {
          log.error("(updateStatus)taskId: {}, userID: {}", taskId, userId);
          throw new TaskNotFoundException();
        });
    taskRepository.updatePoint(task.getId(), point);
    task.setPoint(point);
    return TaskResponse.from(task, userId);
  }

  @Override
  public String getUserIdById(String taskId) {
    log.info("(getUserIdById)taskId: {}", taskId);
    return taskRepository.getUserIdById(taskId);
  }

  @Override
  public TaskResponse updateSprintId(String projectId, String taskId, String sprintId,
      String userId) {
    log.info("(updateSprintId)projectId: {}, taskId: {}, sprintId: {}, userId: {}",
        projectId, taskId, sprintId, userId);
    var task = taskRepository
        .findByProjectIdAndId(projectId, taskId)
        .orElseThrow(() -> {
          log.error("(updateSprintId)projectId: {}, taskId: {}, sprintId: {}, userId: {}",
              projectId, taskId, sprintId, userId);
          throw new TaskNotFoundException();
        });
    task.setSprintId(sprintId);
    taskRepository.save(task);
    return TaskResponse.of(task.getId(), task.getTitle(), task.getPoint(), task.getStatus(),
        task.getKeyProjectTask(), userId);
  }

  @Override
  public boolean existsByUserIdAndTaskId(String userId, String taskId) {
    log.info("(existsByUserIdAndTaskId)");
    return taskRepository.existsByUserIdAndTaskId(userId, taskId);
  }

  @Override
  public boolean existById(String id) {
    log.info("(existById)id: {}", id);
    return taskRepository.existsById(id);
  }

  @Override
  public Task findById(String taskId) {
    log.info("(findById)taskId: {}", taskId);
    return taskRepository.findById(taskId)
        .orElseThrow(() -> {
          log.error("(findById)taskId: {}", taskId);
          throw new TaskNotFoundException();
        });
  }

  @Override
  public Task save(Task task) {
    log.info("(save)task: {}", task);
    return taskRepository.save(task);
  }

  public UpdateDueDateTaskResponse updateDueDate(String projectId, String sprintId, String taskId,
      String dueDate) {
    log.info("(updateDueDate)projectId: {}, sprintId: {}, taskId: {}",
        projectId, sprintId, taskId);
    var task = taskRepository
        .findByProjectIdAndId(projectId, taskId)
        .orElseThrow(() -> {
          log.error("(updateDueDate)projectId: {}, sprintId: {}, taskId: {}",
              projectId, sprintId, taskId);
          throw new TaskNotFoundException();
        });
    task.setStartDate(LocalDate.now());
    task.setDueDate(LocalDate.parse(dueDate));
    taskRepository.save(task);
    return new UpdateDueDateTaskResponse(task.getId(), task.getStatus(), task.getDueDate());
  }

  @Override
  public List<Task> getAllBySprintId(String sprintId) {
    log.info("(getAllBySprintId)sprintId: {}", sprintId);
    return taskRepository.findAllBySprintId(sprintId);
  }

  @Override
  public boolean existsBySprintId(String sprintId) {
    log.info("(existsBySprintId)sprintId: {}", sprintId);
    return taskRepository.existsBySprintId(sprintId);
  }

  @Override
  public boolean existByProjectIdAndTaskId(String projectId, String id) {
    log.info("(existByProjectIdAndTaskId)projectId: {}, id: {}", projectId, id);
    return taskRepository.existsByProjectIdAndId(projectId, id);
  }

  @Override
  @Transactional
  public void deleteTask(String userId, String projectId, String taskId) {
    log.info("(deleteTask)projectId: {}, taskId: {}", projectId, taskId);
    taskRepository.deleteById(taskId);
  }

  @Override
  @Transactional
  public void deleteAllByProjectId(String projectId) {
    log.info("(deleteAllByProjectId)projectId: {}", projectId);
    taskRepository.deleteAllByProjectId(projectId);
  }

  @Override
  public void deleteById(String id) {
    log.info("(deleteById)id: {}", id);
    taskRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void deleteAllBySprintId(String sprintId) {
    log.info("(deleteAllBySprintId)sprintId: {}", sprintId);
    taskRepository.deleteAllBySprintId(sprintId);
  }

  @Override
  public void updateTaskTypeIdByTypeId(String defaultTypeId, String oldTypeId) {
    log.info("(updateTaskTypeIdByTypeId)defaultTypeId: {}, oldTypeId: {}", defaultTypeId,
        oldTypeId);
    taskRepository.updateTaskTypeIdByTypeId(defaultTypeId, oldTypeId);
  }

  @Override
  @Transactional
  public void updateTitle(String taskId, String title) {
    log.info("(updateTitle)taskId: {}, title: {}", taskId, title);
    taskRepository.updateTitleById(taskId, title);
  }

  @Override
  public boolean existsByTypeId(String typeId) {
    log.info("(existsByTypeId)typeId: {}", typeId);
    return taskRepository.existsByTypeId(typeId);
  }

  @Override
  public Task getTaskLastestByProjectId(String projectId) {
    log.info("(getTaskLastestByProjectId)projectId: {}", projectId);
    return taskRepository.getTaskLastestByProjectId(projectId);
  }

  @Override
  public List<Task> getAllTaskAssigneesForUser(String userId) {
    log.info("(getAllTaskAssigneesForUser)userId: {}", userId);
    return taskRepository.getAllTaskAssigneesForUser(userId, TaskStatus.DONE.toString());
  }

  @Override
  public List<Task> searchTask(String searchValue, String typeId,
      String labelId, String status, String assignee, String userId, String projectId,
      String sprintId) {
    log.info("(searchTask)searchValue: {}", searchValue);
    return taskRepository.findAll(FilterTask.getTasksByCriteria(searchValue, typeId,
        labelId, status, assignee, userId, projectId, sprintId));
  }

  @Override
  public Integer countBySprintIdAndProjectIdAndStatusNotDone(String sprintId, String projectId) {
    log.info("(countBySprintIdAndProjectIdAndStatusNotDone)sprintId: {}, projectId: {}", sprintId,
        projectId);
    return taskRepository.countBySprintIdAndProjectIdAndStatusNotDone(sprintId, projectId);
  }

  @Override
  public Integer countBySprintIdAndProjectIdAndStatusDone(String sprintId, String projectId) {
    log.info("(countBySprintIdAndProjectIdAndStatusDone)sprintId: {}, projectId: {}", sprintId,
        projectId);
    return taskRepository.countBySprintIdAndProjectIdAndStatusDone(sprintId, projectId);
  }

  @Override
  public void saveAll(List<Task> tasks) {
    log.info("(saveAll)");
    taskRepository.saveAll(tasks);
  }

  @Override
  public List<Task> findAllByProjectIdAndSprintIdAndStatusNotDone(String projectId,
      String sprintId) {
    log.info("(findAllByProjectIdAndSprintIdAndStatusNotDone)projectId: {}, sprintId: {}",
        projectId, sprintId);
    return taskRepository.findAllByProjectIdAndSprintIdAndStatusNotDone(projectId, sprintId);
  }
}
