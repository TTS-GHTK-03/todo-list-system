package org.ghtk.todo_list.service;

import java.util.List;
import org.ghtk.todo_list.entity.AuthUser;

public interface UserService {
  List<AuthUser> searchUser(String searchValue, List<String> roles, String projectId, String userId);

}
