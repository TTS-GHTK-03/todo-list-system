package org.ghtk.todo_list.base_authrization;

import static org.ghtk.todo_list.constant.RoleProjectUser.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.exception.UserUnauthorizedException;
import org.ghtk.todo_list.repository.ProjectUserRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BaseAuthorizationImpl implements BaseAuthorization{

  private final ProjectUserRepository projectUserRepository;

  public void roleAdmin(String userId, String projectId) {
    log.info("(roleAdmin)Checking role admin for user {}", userId);
    var role = projectUserRepository.findRoleByUserIdAndProjectId(userId, projectId);
    if (role == null || !role.equals(ADMIN.toString())) {
      log.info("(roleAdmin)Role admin for user {} and role: {}", userId, role);
      throw new UserUnauthorizedException();
    }
  }

  public void roleAdminAndEdit(String userId, String projectId) {
    log.info("(roleAdminAndEdit)Checking role admin or edit for user {}", userId);
    var role = projectUserRepository.findRoleByUserIdAndProjectId(userId, projectId);
    if (role == null || (!role.equals(ADMIN.toString()) && !role.equals(EDIT.toString()))) {
      log.info("(roleAdmin)Role admin or edit for user {} and role: {}", userId, role);
      throw new UserUnauthorizedException();
    }
  }

  public void allRole(String userId, String projectId) {
    log.info("(allRole)Checking role admin or edit or view for user {}", userId);
    var role = projectUserRepository.findRoleByUserIdAndProjectId(userId, projectId);
    if (role == null || (!role.equals(ADMIN.toString()) && !role.equals(EDIT.toString())
        && !role.equals(VIEWER.toString()))) {
      log.info("(allRole)Role admin or edit or view for user {} and role: {}", userId, role);
      throw new UserUnauthorizedException();
    }
  }
}
