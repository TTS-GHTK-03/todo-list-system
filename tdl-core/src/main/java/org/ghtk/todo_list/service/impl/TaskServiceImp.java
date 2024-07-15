package org.ghtk.todo_list.service.impl;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.repository.TaskRepository;
import org.ghtk.todo_list.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskServiceImp implements TaskService {

  @Autowired
  private TaskRepository taskRepo;

  @Override
  public List<TaskResponse> getAllTasksByProjectId(String projectId) {
    log.info("(getAllTasksByProjectId)projectId: {}", projectId);
    List<Task> tasks = taskRepo.getAllTasksByProjectId(projectId);
    return tasks.stream()
        .map(task -> new TaskResponse(task.getId(), task.getTitle()))
        .collect(Collectors.toList());

  }

  @Override
  public TaskResponse findById(String taskId) {
    log.info("(getTaskByTaskId)projectId: {}", taskId);
    var task = taskRepo.findById(taskId).orElseThrow(() -> {
      throw new TaskNotFoundException();
    });

    return new TaskResponse(task.getId(), task.getTitle());
  }

  @Override
  public TaskResponse taskAgile(String userId, String title) {
    log.info("(createTask)user: {}", userId);

    Task task = new Task();
    task.setTitle(title);

    Task taskSaved = taskRepo.save(task);
    return new TaskResponse(taskSaved.getId(), taskSaved.getTitle());
  }

}
