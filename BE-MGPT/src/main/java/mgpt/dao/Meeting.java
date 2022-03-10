package mgpt.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "note")
    private String note;
    @Column(name = "meeting_time")
    private int meetingTime;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
