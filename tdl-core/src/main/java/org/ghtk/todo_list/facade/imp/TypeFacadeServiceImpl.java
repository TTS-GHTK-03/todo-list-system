package org.ghtk.todo_list.facade.imp;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.RoleProjectUser;
import org.ghtk.todo_list.entity.Type;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.ProjectUserNotFoundException;
import org.ghtk.todo_list.exception.RoleProjectNotAllowException;
import org.ghtk.todo_list.exception.TypeNotFoundException;
import org.ghtk.todo_list.exception.TypeTitleAlreadyExistedException;
import org.ghtk.todo_list.facade.TypeFacadeService;
import org.ghtk.todo_list.mapper.TypeMapper;
import org.ghtk.todo_list.model.response.TypeResponse;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.ProjectUserService;
import org.ghtk.todo_list.service.TypeService;

@Slf4j
@RequiredArgsConstructor
public class TypeFacadeServiceImpl implements TypeFacadeService {

  private final ProjectService projectService;
  private final ProjectUserService projectUserService;
  private final TypeService typeService;
  private final TypeMapper typeMapper;

  @Override
  public Type createType(String userId, String projectId, String title, String image,
      String description) {
    log.info("(createType)projectId: {}, title: {}, image: {}, description: {}", projectId, title,
        image, description);

    validateExistProjectUser(userId, projectId);
    validateRoleProjectUser(userId, projectId);
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

    validateExistProjectUser(userId, projectId);
    validateRoleProjectUser(userId, projectId);
    validateProjectId(projectId);
    validateTypeId(typeId);
    validateTypeTitle(projectId, title);

    Type type = typeMapper.toType(title, image, description);
    type.setId(typeId);

    return typeService.updateType(type);
  }

  @Override
  public List<TypeResponse> getAllTypes(String userId, String projectId) {
    log.info("(getAllTypes)projectId: {}", projectId);
    validateProjectId(projectId);
    validateExistProjectUser(userId, projectId);
    return typeMapper.toTypeResponses(typeService.findAllByProjectId(projectId));
  }

  @Override
  public TypeResponse getType(String userId, String projectId, String typeId) {
    log.info("(getType)projectId: {}, typeId: {}", projectId, typeId);
    validateProjectId(projectId);
    validateExistProjectUser(userId, projectId);
    validateTypeId(typeId);
    return typeMapper.toTypeResponse(typeService.findById(typeId));
  }

  private void validateExistProjectUser(String userId, String projectId){
    log.info("(validateExistProjectUser)userId: {}, projectId: {}", userId, projectId);
    if(!projectUserService.existsByUserIdAndProjectId(userId, projectId)){
      log.error("(validateExistProjectUser)userId: {}, projectId: {}", userId, projectId);
      throw new ProjectUserNotFoundException();
    }
  }

  private void validateRoleProjectUser(String userId, String projectId){
    log.info("(validateRoleProjectUser)userId: {}, projectId: {}", userId, projectId);
    String roleProjectUser = projectUserService.getRoleProjectUser(userId, projectId);
    System.out.println(roleProjectUser);
    if(!roleProjectUser.equals(RoleProjectUser.ADMIN.toString())){
      log.error("(validateRoleProjectUser)role: {} isn't allowed", roleProjectUser);
      throw new RoleProjectNotAllowException();
    }
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
