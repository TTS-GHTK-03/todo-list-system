package org.ghtk.todo_list.constant;

import lombok.Getter;

@Getter
public enum SprintStatus {
  TODO,
  START,
  COMPLETE;
  public static boolean isValid(String value) {
    for (SprintStatus e : SprintStatus.values()) {
      if (e.name().equalsIgnoreCase(value)) {
        return true;
      }
    }
    return false;
  }
}
