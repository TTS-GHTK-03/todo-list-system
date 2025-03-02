package org.ghtk.todo_list.base_authrization;

public interface BaseAuthorization {

  void roleAdmin(String userId, String projectId) ;

  void roleAdminAndEdit(String userId, String projectId);

  void allRole(String userId, String projectId);
}
