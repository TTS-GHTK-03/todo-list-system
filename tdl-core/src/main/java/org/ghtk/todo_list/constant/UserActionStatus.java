package org.ghtk.todo_list.constant;

public enum UserActionStatus {

  LOGGED_ACCEPTED, ACCEPTED, UNREGISTERED;

  public static boolean isValid(String value){
    for(UserActionStatus userActionStatus : UserActionStatus.values()){
      if(userActionStatus.name().equals(value)){
        return true;
      }
    }
    return false;
  }
}
