package org.ghtk.todo_list.facade;

import java.sql.Time;
import java.util.List;
import org.ghtk.todo_list.dto.response.UserResponse;
import org.ghtk.todo_list.model.response.AcceptInviteResponse;
import org.ghtk.todo_list.model.response.AcceptShareResponse;

public interface ProjectUserFacadeService {
  void inviteUser(String userId, String projectId, String invitedUserEmail, String role, Boolean reSend);
  AcceptInviteResponse accept(String userId, String email, String projectId);

  void shareProject(String userId, String projectId, String sharedUserEmail, String role, long expireTime);
  AcceptShareResponse viewShareProject(String userId, String shareToken);

  List<UserResponse> getAllUserByProject(String userId, String projectId);

  String updateRoleProjectUser(String projectId, String memberId, String role);
  void deleteUser(String userId, String projectId, String memberId);
}
