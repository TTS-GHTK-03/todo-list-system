package org.ghtk.todo_list.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.SprintProgress;
import org.ghtk.todo_list.exception.SprintNotFoundException;
import org.ghtk.todo_list.repository.SprintProgressRepository;
import org.ghtk.todo_list.service.SprintProgressService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class SprintProgressServiceImpl implements SprintProgressService {

  private final SprintProgressRepository sprintProgressRepository;

  @Override
  public SprintProgress save(SprintProgress sprintProgress) {
    log.info("(save)sprintProgress: {}", sprintProgress);
    return sprintProgressRepository.save(sprintProgress);
  }

  @Override
  public void updateCompleteTask(String taskId) {
    log.info("(updateCompleteTask)taskId: {}", taskId);
    Optional<SprintProgress> sprintProgressOpt = sprintProgressRepository.findSprintProgressByTaskId(taskId);
    if (sprintProgressOpt.isPresent()) {
      SprintProgress sprintProgress = sprintProgressOpt.get();
      sprintProgress.setCompleteTask(sprintProgress.getCompleteTask() - 1);
      sprintProgressRepository.save(sprintProgress);
    }
  }

  @Override
  public SprintProgress findBySprintId(String sprintId) {
    log.info("(findBySprintId)sprintId: {}", sprintId);
    return sprintProgressRepository.findBySprintId(sprintId);
  }

  @Override
  @Transactional
  public void deleteAllBySprintId(String sprintId) {
    log.info("(deleteAllBySprintId)sprintId: {}", sprintId);
    sprintProgressRepository.deleteAllBySprintId(sprintId);
  }
}
