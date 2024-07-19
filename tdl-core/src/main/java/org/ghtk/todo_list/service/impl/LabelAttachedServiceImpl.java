package org.ghtk.todo_list.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.repository.LabelAttachedRepository;
import org.ghtk.todo_list.service.LabelAttachedService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LabelAttachedServiceImpl implements LabelAttachedService {
  private final LabelAttachedRepository repository;

  @Override
  public void deleteByLabelId(String labelId) {
    log.info("(deleteByLabelId)labelId: {}", labelId);
    repository.deleteByLabelId(labelId);
  }
}
