package org.ghtk.todo_list.service.impl;

import java.time.LocalDate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.model.response.UpdateDueDateTaskResponse;
import org.ghtk.todo_list.repository.TaskRepository;
import org.ghtk.todo_list.repository.UserProjection;
import org.ghtk.todo_list.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskServiceImp implements TaskService {

  @Autowired
  private TaskRepository taskRepository;


  @Override
  public List<TaskResponse> getAllTasksByProjectId(String projectId) {
    log.info("(getAllTasksByProjectId)projectId: {}", projectId);
    List<Task> tasks = taskRepository.getAllTasksByProjectId(projectId);
    return tasks.stream()
        .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.getPoint(),
            task.getStatus()))
        .collect(Collectors.toList());

  }

  @Override
  public TaskResponse findById(String taskId, String userId) {
    log.info("(getTaskByTaskId)projectId: {}", taskId);
    var task = taskRepository.findById(taskId).orElseThrow(() -> {
      throw new TaskNotFoundException();
    });

    return TaskResponse.of(task.getId(), task.getTitle(), task.getPoint(), task.getStatus(), userId);
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
    return TaskResponse.of(task.getId(), task.getTitle(), task.getPoint(), task.getStatus(), userId);
  }

  @Override
  public String getUserIdById(String taskId) {
    log.info("(getUserIdById)taskId: {}", taskId);
    return taskRepository.getUserIdById(taskId);
  }

  @Override
  public TaskResponse updateSprintId(String projectId, String taskId, String sprintId, String userId) {
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
    return TaskResponse.of(task.getId(), task.getTitle(), task.getPoint(), task.getStatus(), userId);
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
  public UpdateDueDateTaskResponse updateDueDate(String projectId, String sprintId, String taskId, String dueDate){
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
}
