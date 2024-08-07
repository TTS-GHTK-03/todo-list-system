package org.ghtk.todo_list.service.impl;

import static org.ghtk.todo_list.constant.SprintStatus.COMPLETE;

import java.time.LocalDate;
import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;

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

  @Override
  public List<Sprint> findSprintsByProjectId(String projectId) {
    log.info("(findSprintsByProjectId)");
    return sprintRepository.findByProjectId(projectId);
  }

  @Override
  public List<Sprint> findSprintsByProjectIdAndStatus(String projectId, String status) {
    log.info("(findSprintsByProjectIdAndStatus)");
    return sprintRepository.findByProjectIdAndStatus(projectId, status);
  }

  @Override
  public boolean existById(String id) {
    log.info("(existById)id: {}", id);
    return sprintRepository.existsById(id);
  }

  @Override
  public boolean existsByProjectIdAndTitle(String projectId, String title) {
    log.info("(existsByProjectIdAndTitle)");
    return sprintRepository.existsByProjectIdAndTitle(projectId, title);
  }

  @Override
  @Transactional
  public void deleteAllByProjectId(String projectId) {
    log.info("(deleteAllByProjectId)projectId: {}", projectId);
    sprintRepository.deleteAllByProjectId(projectId);
  }


  @Override
  public void deleteById(String id) {
    log.info("(deleteById)");
    sprintRepository.deleteById(id);
  }

  @Override
  public void updateSprintComplete(String id) {
    log.info("(updateSprintComplete)id: {}", id);
    Sprint sprint = sprintRepository
        .findById(id)
        .orElseThrow(() -> {
          log.error("(updateSprintComplete)projectId: {} not found", id);
          throw new SprintNotFoundException();
        });
    sprint.setStatus(COMPLETE.toString());
    sprint.setEndDate(LocalDate.now());
    sprintRepository.save(sprint);
  }
}
