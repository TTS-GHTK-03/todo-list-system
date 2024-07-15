package org.ghtk.todo_list.service.impl;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.SprintStatus;
import org.ghtk.todo_list.entity.AuthAccount;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.exception.ProjectIdMismatchException;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.SprintNotFoundException;
import org.ghtk.todo_list.mapper.SprintMapper;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.ghtk.todo_list.model.response.StartSprintResponse;
import org.ghtk.todo_list.repository.ProjectRepository;
import org.ghtk.todo_list.repository.SprintRepository;
import org.ghtk.todo_list.service.SprintService;

@Slf4j
@RequiredArgsConstructor
public class SprintServiceImpl implements SprintService {

  private final SprintRepository sprintRepository;

  @Override
  public Sprint save(Sprint sprint) {
    log.info("(save)Sprint: {}", sprint);
    return sprintRepository.save(sprint);
  }

  @Override
  public Sprint findById(String id) {
    log.info("findById: {}", id);
    return sprintRepository.findById(id).orElseThrow(() -> {
      log.error("(findById)projectId: {} not found", id);
      throw new SprintNotFoundException();
    });
  }
}
