package org.ghtk.todo_list.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Label;
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
  public boolean existByTypeIdAndTitle(String typeId, String title) {
    return labelRepository.existsByTypeIdAndTitle(typeId, title);
  }
}
