package org.ghtk.todo_list.constant;

public class CacheConstant {

    private CacheConstant() {}

    public static final String RESET_PASSWORD_OTP_KEY = "_RESET_PASSWORD_OTP";
    public static final String OTP_VERIFICATION_ACCOUNT_KEY = "_OTP_VERIFICATION_ACCOUNT_KEY";
    public static final String OTP_VERIFICATION_EMAIL_KEY = "_OTP_VERIFICATION_EMAIL_KEY";
    public static final String FAILED_OTP_ATTEMPT_KEY = "FAILED_OTP_ATTEMPT";

    public static final String OTP_FAILED_UNLOCK_TIME_KEY = "OTP_FAILED_UNLOCK_TIME";

    public static final String LOGIN_FAILED_ATTEMPT_KEY = "LOGIN_FAILED_ATTEMPT";
    public static final String LOGIN_UNLOCK_TIME_KEY = "LOGIN_UNLOCK_TIME";

    public static final String REGISTER_KEY = "REGISTER_KEY";

    public static final String RESET_PASSWORD_KEY = "RESET_PASSWORD_KEY";
    public static final long OTP_TTL_MINUTES = 3L;
    public static final long MAIL_TTL_MINUTES = 5L;

    public static final String INVITE_KEY = "INVITE_KEY";
    public static final String SHARE_KEY = "SHARED_KEY";
    public static final String UPDATE_STATUS_TASK = "UPDATE_STATUS_TASK";
    public static final String UPDATE_STATUS_TASK_KEY = "_UPDATE_STATUS_TASK_KEY";
    public static final String REGISTER_SHARE_KEY = "REGISTER_SHARE_KEY";
}
