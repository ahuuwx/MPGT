package mgpt.util;

import lombok.SneakyThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

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
    public static final int LECTURER_ROLE_ID = 1;
    public static final int MEMBER_ROLE_ID = 2;
    public static final int LEADER_ROLE_ID = 3;
    public static final String LECTURER_ROLE_NAME = "Lecturer";
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
     * TASK COMMENT
     */
    public static final String NULL_TASK_COMMENT = "TASK COMMENT IS NOT NULL";
    public static final String INVALID_TASK_COMMENT = "TASK COMMENT IS NOT VALID";
    /**
     * SPRINT
     */
    public static final String INVALID_SPRINT = "INVALID SPRINT";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String NULL_SPRINT_NAME = "SPRINT NAME CAN NOT BE NULL";
    public static final String NULL_UPDATE_SPRINT_REASON = "UPDATE SPRINT REASON CAN NOT BE NULL";


    /**
     * PROJECT
     */
    public static final String INVALID_PROJECT_ID = "INVALID PROJECT ID";

    /**
     * DATE
     */
    public static final String INVALID_STARTDATE_ENDDATE = "START DATE CAN NOT AFTER END DATE";
    /**
     * FireBase
     */
    public static final String BUCKET_NAME = "mpgt-2022.appspot.com";
    public static final String PROJECT_ID = "mpgt-2022";
    /**
     * MEETING
     */
    public static final String MEETING_NOT_NULL = "MEETINGS IN PROJECT ARE NOT NULL, CAN NOT CREATE MORE MEETINGS";
    public static final String INVALID_MEETING = "INVALID MEETING";
    public static final String MEETING_LINK_NOT_NULL = "MEETING LINK IS NOT NULL";
    public static final String MEETING_NULL = "MEETING IS NULL";
    public static final String MEETING_NOTE_NULL="MEETING NOTE CAN NOT BE NULL";
    public static final String MEETING_NOTE_CAN_NOT_BE_UPDATE_DATE_BEFORE = "CAN NOT UPDATE MEETING NOTE BECAUSE THE MEETING HAS NOT OCCURRED";
    public static final String MEETING_NOTE_HAS_NOT_UPDATE_IN_PRE_MEETING="YOU HAVEN'T UPDATE THE NOTE IN THIS PREVIOUS MEETING";
    /**
     * Conversion
     */
    public static final long PLUS_HOUR_FROM_UTC_TO_UTC7 = 7;

    // <editor-fold desc="Convert to UTC+7 TimeZone">
    public static Date convertToUTC7TimeZone(Date insDate) {
        ZonedDateTime date = ZonedDateTime.ofInstant(insDate.toInstant(), ZoneId.of(TIMEZONE))
                .plusHours(PLUS_HOUR_FROM_UTC_TO_UTC7);
        return Date.from(date.toInstant());
    }
    // </editor-fold>

}
