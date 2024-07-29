package org.ghtk.todo_list.facade;

import java.sql.Time;
import java.util.List;
import org.ghtk.todo_list.dto.response.AuthUserResponse;

public interface ProjectUserFacadeService {
  void inviteUser(String userId, String projectId, String email, String role);
  String accept(String email);

  void shareProject(String userId, String projectId, String email, String role, Time expireDate);
  void viewShareProject(String shareToken);

  List<AuthUserResponse> getAllUserByProject(String userId, String projectId);
  void deleteUser(String userId, String projectId, String memberId);
}
