package org.ghtk.todo_list.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.TaskAssignees;
import org.ghtk.todo_list.exception.SprintNotFoundException;
import org.ghtk.todo_list.repository.TaskAssigneesRepository;
import org.ghtk.todo_list.service.TaskAssigneesService;
import org.ghtk.todo_list.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class TaskAssigneesServiceImpl implements TaskAssigneesService {

  private final TaskAssigneesRepository taskAssigneesRepository;

  @Override
  public String findUserIdByTaskId(String taskId) {
    log.info("(findUserIdByTaskId)taskId: {}", taskId);
    return taskAssigneesRepository.findUserIdByTaskId(taskId);
  }

  @Override
  public TaskAssignees findById(String id) {
    log.info("(findById)id: {}", id);
    return taskAssigneesRepository.findById(id).orElseThrow(() -> {
      log.error("(findById)id: {} not found", id);
      throw new SprintNotFoundException();
    });
  }

  @Override
  public TaskAssignees save(TaskAssignees taskAssignees) {
    log.info("(save)TaskAssignees: {}", taskAssignees);
    return taskAssigneesRepository.save(taskAssignees);
  }

  @Override
  public boolean existsByUserIdAndTaskId(String userId, String taskId) {
    return taskAssigneesRepository.existsByUserIdAndTaskId(userId, taskId);
  }

  @Override
  @Transactional
  public void deleteAllByTaskId(String taskId) {
    taskAssigneesRepository.deleteAllByTaskId(taskId);
  }

  @Override
  public void updateTaskAssigneesByUserIdAndProjectId(String userId, String memberId,
      String projectId) {
    log.info("(updateTaskAssigneesByUserIdAndProjectId)userId: {}, memberId: {}, projectId: {}", userId, memberId, projectId);
    taskAssigneesRepository.updateTaskAssigneesByUserIdAndProjectId(userId, memberId, projectId);
  }

  @Override
  public TaskAssignees findByTaskId(String taskId) {
    log.info("(findByTaskId)taskId: {}", taskId);
    return taskAssigneesRepository.findByTaskId(taskId);
  }
}
