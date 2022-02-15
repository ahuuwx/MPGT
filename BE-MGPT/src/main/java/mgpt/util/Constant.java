package mgpt.util;

public class Constant {
    /**
     * JWT
     */
    public static final String SECRET_KEY = "MPGT_FA21#MPGT";
    public static final String STRING_AUTHORIZATION = "Authorization";
    public static final String URL_LOGIN = "/login";
    public static final String URL_GET_PROFILE = "/get-profile**";

    /**
     * ROLE
     */
    public static final int LECTURER_ROLE_ID = 2;
    public static final int ADMIN_ROLE_ID = 1;
    public static final int PDT_ROLE_ID = 3;
    public static final int MEMBER_ROLE_ID = 4;
    public static final int LEADER_ROLE_ID = 5;
    public static final String LECTURER_ROLE_NAME = "Lecturer";
    public static final String ADMIN_ROLE_NAME = "Admin";
    public static final String PDT_ROLE_NAME = "PDT";
    public static final String MEMBER_ROLE_NAME = "Member";
    public static final String LEADER_ROLE_NAME = "Leader";
    /**
     * ACCOUNT
     */
    public static final String INVALID_USERNAME = "INVALID USERNAME";
    public static final String INVALID_ASSIGNEEUSERNAME = "INVALID ASSIGNEE USERNAME";
    /**
     * TASK
     */
    public static final String INVALID_TASKNAME = "INVALID TASK NAME";
    public static final String INVALID_TASKID = "INVALID TASK ID";
    public static final String TIMEZONE = "Asia/Ho_Chi_Minh";
    /**
     * TASK STATUS
     */
    public static final String INVALID_TASK_STATUS = "INVALID TASK STATUS";
    /**
     * TASK
     */
    public static final String NULL_TASK_COMMENT = "TASK COMMENT IS NOT NULL";
    /**
     * SPRINT
     */
    public static final String INVALID_SPRINT = "INVALID SPRINT";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String NULL_SPRINT_NAME = "SPRINT NAME CAN NOT BE NULL";


    /**
     * PROJECT
     */
    public static final String INVALID_PROJECT_ID = "INVALID PROJECT ID";
}
