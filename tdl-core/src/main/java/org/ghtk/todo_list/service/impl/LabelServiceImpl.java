package org.ghtk.todo_list.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Label;
import org.ghtk.todo_list.exception.LabelNotFoundException;
import org.ghtk.todo_list.exception.TypeNotFoundException;
import org.ghtk.todo_list.mapper.LabelMapper;
import org.ghtk.todo_list.model.response.LabelResponse;
import org.ghtk.todo_list.repository.LabelRepository;
import org.ghtk.todo_list.service.LabelService;
import org.ghtk.todo_list.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

  private final LabelRepository labelRepository;
  private final LabelMapper labelMapper;

  @Override
  public Label save(Label label) {
    log.info("(save)label: {}", label);
    return labelRepository.save(label);
  }

  @Override
  public Label findById(String id) {
    log.info("(findById)labelId: {}", id);
    return labelRepository.findById(id).orElseThrow(() -> {
      log.error("(findById)labelId: {} not found", id);
      throw new LabelNotFoundException();
    });
  }

  @Override
  public boolean existByTypeIdAndTitle(String typeId, String title) {
    log.info("(existByTypeIdAndTitle)typeId: {}, title: {}", typeId, title);
    return labelRepository.existsByTypeIdAndTitle(typeId, title);
  }

  @Override
  public List<Label> getLabelsByType(String typeId) {
    log.info("(getLabelsByType)typeId: {}", typeId);
    return labelRepository.findByTypeId(typeId);
  }

  @Override
  public List<Label> getAllLabelByProjectIdAndLabelAttached(String projectId) {
    log.info("(getAllLabelByProjectIdAndLabelAttached)projectId: {}", projectId);
    return labelRepository.getAllLabelByProjectIdAndLabelAttached(projectId);
  }

  @Override
  public List<LabelResponse> getAllLabelByTask(String taskId) {
    log.info("(getAllLabelByTask)taskId: {}", taskId);
    return labelMapper.toLabelResponses(labelRepository.getAllLabelByTask(taskId));
  }

  @Override
  public void deleteLabel(String id) {
    log.info("(deleteLabel)id: {}", id);
    labelRepository.deleteById(id);
  }

  @Override
  public void deleteByTypeId(String typeId) {
    log.info("(deleteByTypeId)typeId: {}", typeId);
    labelRepository.deleteByTypeId(typeId);
  }

  @Override
  public boolean existsById(String id) {
    log.info("(existsById)id: {}", id);
    return labelRepository.existsById(id);
  }

  @Override
  @Transactional
  public void deleteAllByTypeId(String typeId) {
    log.info("(deleteAllByTypeId)typeId: {}", typeId);
    labelRepository.deleteAllByTypeId(typeId);
  }

  @Override
  public boolean existsByTypeId(String typeId) {
    log.info("(existsByTypeId)typeId: {}", typeId);
    return labelRepository.existsByTypeId(typeId);
  }

  @Override
  public boolean existsByTypeIdAndLabelId(String typeId, String labelId) {
    log.info("(existsByTypeIdAndLabelId)typeId: {}, labelId: {}", typeId, labelId);
    return labelRepository.existsByTypeIdAndId(typeId, labelId);
  }
}
