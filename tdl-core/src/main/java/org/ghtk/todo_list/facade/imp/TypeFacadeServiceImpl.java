package org.ghtk.todo_list.facade.imp;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Type;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.TypeNotFoundException;
import org.ghtk.todo_list.exception.TypeTitleAlreadyExistedException;
import org.ghtk.todo_list.facade.TypeFacadeService;
import org.ghtk.todo_list.mapper.TypeMapper;
import org.ghtk.todo_list.model.response.TypeResponse;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.TypeService;

@Slf4j
@RequiredArgsConstructor
public class TypeFacadeServiceImpl implements TypeFacadeService {

  private final ProjectService projectService;
  private final TypeService typeService;
  private final TypeMapper typeMapper;

  @Override
  public Type createType(String userId, String projectId, String title, String image,
      String description) {
    log.info("(createType)projectId: {}, title: {}, image: {}, description: {}", projectId, title,
        image, description);

    validateProjectId(projectId);
    validateTypeTitle(projectId, title);

    Type type = typeMapper.toType(title, image, description);
    type.setProjectId(projectId);

    return typeService.createType(type);
  }

  @Override
  public Type updateType(String userId, String projectId, String typeId, String title, String image,
      String description) {
    log.info("(updateType)userId: {}, projectId: {}, typeId: {}, title: {}, image: {}, description: {}",
        userId, projectId, typeId, title, image, description);

    validateProjectId(projectId);
    validateTypeId(typeId);
    validateTypeTitle(projectId, title);

    Type type = typeMapper.toType(title, image, description);
    type.setId(typeId);

    return typeService.updateType(type);
  }

  @Override
  public List<TypeResponse> getAllTypes(String projectId) {
    log.info("(getAllTypes)projectId: {}", projectId);

    validateProjectId(projectId);

    List<Type> typeList = typeService.findAllByProjectId(projectId);
    List<TypeResponse> typeResponseList = typeMapper.toTypeResponses(typeList);

    return typeMapper.toTypeResponses(typeList);
  }

  private void validateProjectId(String projectId) {
    log.info("(validateProjectId)projectId: {}", projectId);
    if (!projectService.existById(projectId)) {
      log.error("(validateProjectId)projectId: {} not found", projectId);
      throw new ProjectNotFoundException();
    }
  }

  private void validateTypeId(String typeId) {
    log.info("(validateTypeId)typeId: {}", typeId);
    if (!typeService.existById(typeId)) {
      log.error("(validateTypeId)typeId: {} not found", typeId);
      throw new TypeNotFoundException();
    }
  }

  private void validateTypeTitle(String projectId, String title) {
    log.info("(validateTypeTitle)projectId: {}, title: {}", projectId, title);
    if (typeService.existByProjectIdAndTitle(projectId, title)) {
      log.error("(validateTypeTitle)title: {} already existed in projectId: {}", title, projectId);
      throw new TypeTitleAlreadyExistedException();
    }
  }
}
