package org.ghtk.todo_list.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Label;
import org.ghtk.todo_list.exception.LabelNotFoundException;
import org.ghtk.todo_list.exception.TypeNotFoundException;
import org.ghtk.todo_list.repository.LabelRepository;
import org.ghtk.todo_list.service.LabelService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

  private final LabelRepository labelRepository;

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
}
