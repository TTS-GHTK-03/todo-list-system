package org.ghtk.todo_list.facade.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Type;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.TypeTitleAlreadyExistedException;
import org.ghtk.todo_list.facade.TypeFacadeService;
import org.ghtk.todo_list.mapper.TypeMapper;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.TypeService;

@Slf4j
@RequiredArgsConstructor
public class TypeFacadeServiceImpl implements TypeFacadeService {

  private final ProjectService projectService;
  private final TypeService typeService;
  private final TypeMapper typeMapper;

  @Override
  public Type createType(String projectId, String title, String image, String description) {
    log.info("(createType)projectId: {}, title: {}, image: {}, description: {}", projectId, title,
        image, description);

    validateProjectId(projectId);
    validateTypeTitle(projectId, title);

    Type type = typeMapper.toType(title, image, description);
    type.setProjectId(projectId);

    return typeService.createType(type);
  }

  void validateProjectId(String projectId){
    log.info("(validateProjectId)projectId: {}", projectId);
    if(!projectService.existById(projectId)){
      log.error("(validateProjectId)projectId: {} not found", projectId);
      throw new ProjectNotFoundException();
    }
  }

  void validateTypeTitle(String projectId, String title){
    log.info("(validateTypeTitle)projectId: {}, title: {}", projectId, title);
    if(typeService.existByProjectIdAndTitle(projectId, title)){
      log.error("(validateTypeTitle)title: {} already existed in projectId: {}", title, projectId);
      throw new TypeTitleAlreadyExistedException();
    }
  }
}
