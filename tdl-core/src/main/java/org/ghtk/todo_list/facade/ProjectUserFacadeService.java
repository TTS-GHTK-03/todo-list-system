package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.dto.response.AuthUserResponse;

public interface ProjectUserFacadeService {
  void inviteUser(String userId, String projectId, String email, String role);
  String accept(String email);
  List<AuthUserResponse> getAllUserByProject(String userId, String projectId);
  void deleteUser(String userId, String projectId, String memberId);
}
