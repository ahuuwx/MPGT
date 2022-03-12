package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MeetingsResponseDto {
    private int meetingId;
    private String meetingLink;
    private Date meetingDate;
    private int meetingTime;
    private String note;
    private int projectId;
}
