package org.ghtk.todo_list.service.impl;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.repository.TaskRepository;
import org.ghtk.todo_list.repository.UserProjection;
import org.ghtk.todo_list.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.getPoint(), task.getStatus()))
        .collect(Collectors.toList());

  }

  @Override
  public List<Task> getAllTasksByProjectIdAndStatus(String projectId, String status) {
    log.info("(getAllTasksByProjectIdAndStatus)");
    return taskRepository.getAllTasksByProjectIdAndStatus(projectId, status);
  }

  @Override
  public TaskResponse findById(String taskId, UserProjection userProjection) {
    log.info("(getTaskByTaskId)projectId: {}", taskId);
    var task = taskRepository.findById(taskId).orElseThrow(() -> {
      throw new TaskNotFoundException();
    });

    return new TaskResponse(task.getId(), task.getTitle(), task.getPoint(), task.getStatus(), userProjection);
  }

  @Override
  public TaskResponse updateStatus(String taskId, String taskStatus, UserProjection userProjection) {
    log.info("(updateStatus)taskId: {}, status: {}", taskId, taskStatus);
    var task = taskRepository
        .findById(taskId)
        .orElseThrow(() -> {
          log.error("(updateStatus)taskId: {}, status: {}", taskId, taskStatus);
          throw new TaskNotFoundException();
        });
    task.setStatus(taskStatus);
    taskRepository.save(task);
    return new TaskResponse(task.getId(), task.getTitle(), task.getPoint(), task.getStatus(), userProjection);
  }

  @Override
  public String getUserIdById(String taskId) {
    log.info("(getUserIdById)taskId: {}", taskId);
    return taskRepository.getUserIdById(taskId);
  }

  @Override
  public TaskResponse updateSprintId(String projectId, String taskId, String sprintId,
      UserProjection userProjection) {
    log.info("(updateSprintId)projectId: {}, taskId: {}, sprintId: {}, userProjection: {}",
        projectId, taskId, sprintId, userProjection);
    var task = taskRepository
        .findByProjectIdAndId(projectId, taskId)
        .orElseThrow(() -> {
          log.error("(updateSprintId)projectId: {}, taskId: {}, sprintId: {}, userProjection: {}",
              projectId, taskId, sprintId, userProjection);
          throw new TaskNotFoundException();
        });
    task.setSprintId(sprintId);
    task.setProjectId(null);
    taskRepository.save(task);
    return new TaskResponse(task.getId(), task.getTitle(), task.getPoint(), task.getStatus(), userProjection);
  }

  @Override
  public boolean existsByUserIdAndTaskId(String userId, String taskId) {
    log.info("(existsByUserIdAndTaskId)");
    return taskRepository.existsByUserIdAndTaskId(userId, taskId);
  }

}
