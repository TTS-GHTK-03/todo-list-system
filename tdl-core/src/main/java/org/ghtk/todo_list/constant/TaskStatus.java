package org.ghtk.todo_list.constant;

public enum TaskStatus {
    TODO, IN_PROGRESS, READY_FOR_TEST, DONE;

    public static boolean isValid(String value) {
        for (TaskStatus e : TaskStatus.values()) {
            if (e.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}