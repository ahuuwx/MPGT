package mgpt.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mgpt.model.ProjectListResponseDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "project")
public class Project {
    @Id
    @Column(name = "project_id")
    private int projectId;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;

    @OneToMany(mappedBy = "projectId")
    @JsonIgnore
    private List<Sprint> sprintList;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private List<Meeting> meetingList;
    @OneToMany(mappedBy = "project")
    @JsonIgnore

    private List<ProjectOfUser> projectOfUserList;
    // <editor-fold desc="Convert to ProjectDto">
    public ProjectListResponseDto convertToProjectDto() {
        ProjectListResponseDto projectListResponseDto = new ProjectListResponseDto();
        projectListResponseDto.setProjectId(projectId);
        projectListResponseDto.setProjectName(projectName);
        projectListResponseDto.setLeaderName("");
        return projectListResponseDto;
    }
    // </editor-fold>
}
