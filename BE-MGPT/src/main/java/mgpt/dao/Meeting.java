package mgpt.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mgpt.model.MeetingsResponseDto;
import mgpt.util.Constant;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "meeting")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private int meetingId;
    @Column(name = "meeting_link")
    private String meetingLink;
    @Column(name = "meeting_date")
    private Date meetingDate;
    @Column(name = "file_url")
    private String fileUrl;
    @Column(name = "meeting_time")
    private int meetingTime;
    @Column(name = "is_note")
    private boolean isNote;

    public boolean isNote() {
        return isNote;
    }

    public void setIsNote(boolean note) {
        isNote = note;
    }

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public MeetingsResponseDto convertToDto(){
        MeetingsResponseDto dto = new MeetingsResponseDto();
        dto.setMeetingId(meetingId);
        dto.setMeetingLink(meetingLink);
        dto.setMeetingDate(Constant.convertToUTC7TimeZone(meetingDate));
        dto.setMeetingTime(meetingTime);
        dto.setNote(fileUrl);
        dto.setProjectId(project.getProjectId());

        return dto;
    }
}
