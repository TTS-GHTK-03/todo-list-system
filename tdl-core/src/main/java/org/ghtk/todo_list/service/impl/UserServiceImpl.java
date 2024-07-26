package org.ghtk.todo_list.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.filter.FilterUser;
import org.ghtk.todo_list.repository.AuthUserRepository;
import org.ghtk.todo_list.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final AuthUserRepository authUserRepository;

  @Override
  public List<AuthUser> searchUser(String searchValue, List<String> roles, String projectId,
      String userId) {
    log.info("(searchUser)searchValue: {}, roles: {}, projectId: {}", searchValue, roles, projectId);
    return authUserRepository.findAll(FilterUser.getUsersByCriteria(searchValue, roles, projectId, userId));
  }
}
