package org.ghtk.todo_list.constant;

public class CacheConstant {

    private CacheConstant() {}

    public static final String RESET_PASSWORD_OTP_KEY = "_RESET_PASSWORD_OTP";
    public static final String OTP_ACTIVE_ACCOUNT_KEY = "_OTP_ACTIVE_ACCOUNT";

    public static final String FAILED_OTP_ATTEMPT_KEY = "FAILED_OTP_ATTEMPT";

    public static final String OTP_FAILED_UNLOCK_TIME_KEY = "OTP_FAILED_UNLOCK_TIME";

    public static final String LOGIN_FAILED_ATTEMPT_KEY = "LOGIN_FAILED_ATTEMPT";
    public static final String LOGIN_UNLOCK_TIME_KEY = "LOGIN_UNLOCK_TIME";

    public static final String RESET_PASSWORD_KEY = "RESET_PASSWORD_KEY";
    public static final long OTP_TTL_MINUTES = 3L;
}
