package mgpt.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mgpt.model.SprintDto;
import mgpt.model.SprintListResponseDto;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "sprint")
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprint_id")
    private int sprintId;
    @Column(name = "sprint_name")
    private String sprintName;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "score")
    private float score;
    @Column(name = "file_url")
    private String fileUrl;
    @Column(name = "update_reason")
    private String reason;


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project projectId;

    //<editor-fold desc="Convert To Sprint List Dto">
    public SprintListResponseDto convertToSprintListDto() {
        SprintListResponseDto sprintListResponseDto = new SprintListResponseDto();
        sprintListResponseDto.setSprintId(sprintId);
        sprintListResponseDto.setSprintName(sprintName);
        sprintListResponseDto.setStartDate(startDate);
        sprintListResponseDto.setEndDate(endDate);
        sprintListResponseDto.setFileUrl(fileUrl);
        sprintListResponseDto.setScore(score);
        sprintListResponseDto.setReason(reason);
        return sprintListResponseDto;
    }
    //</editor-fold>

    public SprintDto convertToSprintDto() {
        SprintDto sprintDto = new SprintDto();
        sprintDto.setSprintId(sprintId);
        sprintDto.setSprintName(sprintName);
        return sprintDto;
    }

}
