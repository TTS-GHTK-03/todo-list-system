package org.ghtk.todo_list.constant;

public class ActivityLogConstant {

  public static class TaskAction {
    public static final String CREATE_TASK = "CREATE_TASK";
    public static final String UPDATE_TASK = "UPDATE_TASK";
    public static final String CLONE_TASK = "CLONE_TASK";
    public static final String UPDATE_STATUS_TASK = "UPDATE_STATUS_TASK";
    public static final String UPDATE_SPRINT_TASK = "UPDATE_SPRINT_TASK";
    public static final String UPDATE_DUE_DATE_TASK = "UPDATE_DUE_DATE_TASK";
    public static final String DELETE_TASK = "DELETE_TASK";
  }

  public static class ProjectAction {
    public static final String UPDATE_PROJECT = "UPDATE_PROJECT";
    public static final String DELETE_PROJECT = "DELETE_PROJECT";
  }

  public static class CommentAction {
    public static final String CREATE_COMMENT = "CREATE_COMMENT";
    public static final String REPLY_COMMENT = "REPLY_COMMENT";
  }

  public static class SprintAction {
    public static final String CREATE_SPRINT = "CREATE_SPRINT";
    public static final String DELETE_SPRINT = "DELETE_SPRINT";
    public static final String START_SPRINT = "START_SPRINT";
    public static final String COMPLETE_SPRINT = "COMPLETE_SPRINT";
  }

  public static class AssigneeAction {
    public static final String ADD_ASSIGNEE = "ADD_ASSIGNEE";
  }

  public static class ProjectUserAction {
    public static final String INVITE_USER = "INVITE_USER";
    public static final String ACCEPT_INVITE = "ACCEPT_INVITE";
    public static final String KICK_USER = "KICK_USER";
  }

}
