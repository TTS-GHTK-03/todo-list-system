package org.ghtk.todo_list.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.repository.TaskAssigneesRepository;
import org.ghtk.todo_list.service.TaskAssigneesService;

@Slf4j
@RequiredArgsConstructor
public class TaskAssigneesServiceImpl implements TaskAssigneesService {

  private final TaskAssigneesRepository taskAssigneesRepository;

  @Override
  public String findUserIdByTaskId(String taskId) {
    log.info("(findUserIdByTaskId)taskId: {}", taskId);
    return taskAssigneesRepository.findUserIdByTaskId(taskId);
  }
}
