package org.ghtk.todo_list.constant;

public enum RoleProjectUser {
    ADMIN, EDIT, VIEWER;

    public static boolean isValid(String value){
        for(RoleProjectUser roleProjectUser : RoleProjectUser.values()){
            if(roleProjectUser.name().equals(value)){
                return true;
            }
        }
        return false;
    }
}