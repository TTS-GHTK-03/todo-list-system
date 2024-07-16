package org.ghtk.todo_list.facade;

public interface ProjectUserFacadeService {
  void inviteUser(String userId, String projectId, String email, String role);
  String accept(String email);
}
