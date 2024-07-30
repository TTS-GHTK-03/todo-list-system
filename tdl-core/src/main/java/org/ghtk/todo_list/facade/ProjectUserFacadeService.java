package org.ghtk.todo_list.facade;

import java.sql.Time;
import java.util.List;
import org.ghtk.todo_list.dto.response.UserResponse;

public interface ProjectUserFacadeService {
  void inviteUser(String userId, String projectId, String email, String role, Boolean reSend);
  String accept(String userId, String email, String projectId);

  void shareProject(String userId, String projectId, String email, String role, Time expireDate);
  String viewShareProject(String userId, String shareToken);

  List<UserResponse> getAllUserByProject(String userId, String projectId);

  String updateRoleProjectUser(String projectId, String memberId, String role);
  void deleteUser(String userId, String projectId, String memberId);
}
