package org.ghtk.todo_list.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Type;
import org.ghtk.todo_list.exception.SprintNotFoundException;
import org.ghtk.todo_list.exception.TypeNotFoundException;
import org.ghtk.todo_list.repository.TypeRepository;
import org.ghtk.todo_list.service.TypeService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {

  private final TypeRepository typeRepository;

  @Override
  public boolean existById(String id) {
    return typeRepository.existsById(id);
  }

  @Override
  public Type findById(String id) {
    return typeRepository.findById(id).orElseThrow(() -> {
      log.error("(findById)typeId: {} not found", id);
      throw new TypeNotFoundException();
    });
  }
}
