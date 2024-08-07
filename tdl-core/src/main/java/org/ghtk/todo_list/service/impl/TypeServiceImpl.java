package org.ghtk.todo_list.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Type;
import org.ghtk.todo_list.exception.SprintNotFoundException;
import org.ghtk.todo_list.exception.TypeNotFoundException;
import org.ghtk.todo_list.repository.TypeRepository;
import org.ghtk.todo_list.service.TypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {

  private final TypeRepository typeRepository;

  @Override
  public Type createType(Type type) {
    log.info("(createType)type: {}", type);
    return typeRepository.save(type);
  }

  @Override
  public Type updateType(Type type) {
    log.info("(updateType)type: {}", type);
    return typeRepository.save(type);
  }

  @Override
  public boolean existById(String id) {
    return typeRepository.existsById(id);
  }

  @Override
  public boolean existByProjectIdAndTitle(String projectId, String title) {
    log.info("(existByProjectIdAndTitle)title: {}", title);
    return typeRepository.existsByProjectIdAndTitle(projectId, title);
  }

  @Override
  public boolean existsByIdAndProjectId(String typeId, String projectId) {
    log.info("(existByProjectIdAndTitle)typeId: {}, projectId: {}",typeId, projectId);
    return typeRepository.existsByIdAndProjectId(typeId, projectId);
  }

  @Override
  public Type findById(String id) {
    return typeRepository.findById(id).orElseThrow(() -> {
      log.error("(findById)typeId: {} not found", id);
      throw new TypeNotFoundException();
    });
  }

  @Override
  public List<Type> findAllByProjectId(String projectId) {
    log.info("(findAllByProjectId)projectId: {}", projectId);
    return typeRepository.findAllByProjectId(projectId);
  }

  @Override
  @Transactional
  public void deleteAllByProjectId(String projectId) {
    log.info("(deleteAllByProjectId)projectId: {}", projectId);
    typeRepository.deleteAllByProjectId(projectId);
  }

  @Override
  public Type findByProjectIdAndTitle(String projectId, String title) {
    log.info("(findIdByProjectIdAndTitle)projectId: {}, title: {}", projectId, title);
    System.out.println(typeRepository.findByProjectIdAndTitle(projectId, title));
    return typeRepository.findByProjectIdAndTitle(projectId, title);
  }

  @Override
  public void deleteById(String typeId) {
    log.info("(deleteById)typeId: {}", typeId);
    typeRepository.deleteById(typeId);
    if(typeRepository.existsById(typeId)){
      log.error("(deleteById)typeId: {} isn't deleted", typeId);
    }
  }
}
