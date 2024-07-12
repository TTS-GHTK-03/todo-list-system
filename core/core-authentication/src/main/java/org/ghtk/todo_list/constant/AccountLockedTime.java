package org.ghtk.todo_list.constant;

public enum AccountLockedTime {
  FIVE(5, 300),
  TEN(10, 600),

  FIFTEEN(15,null);

  private final int attempts;
  private final Integer cooldownTime;

  AccountLockedTime(int attempts, Integer cooldownTime) {
    this.attempts = attempts;
    this.cooldownTime = cooldownTime;
  }

  public Integer getAttempts() {
    return attempts;
  }

  public int getCooldownTime() {
    return cooldownTime;
  }
}
