package mgpt.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mgpt.model.ProjectDetailResponseDto;
import mgpt.model.ProjectListResponseDto;

import javax.persistence.*;
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
    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "projectId", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Sprint> sprintList;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Meeting> meetingList;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProjectOfUser> projectOfUserList;

    // <editor-fold desc="Convert to ProjectDto"
    public ProjectListResponseDto convertToProjectDto() {
        ProjectListResponseDto projectListResponseDto = new ProjectListResponseDto();
        projectListResponseDto.setProjectId(projectId);
        projectListResponseDto.setProjectName(projectName);
        return projectListResponseDto;
    }
    // </editor-fold>

    // <editor-fold desc="Convert to ProjectDetailDto"
    public ProjectDetailResponseDto convertToProjectDetailDto() {
        ProjectDetailResponseDto projectDetailResponseDto = new ProjectDetailResponseDto();
        projectDetailResponseDto.setProjectId(projectId);
        projectDetailResponseDto.setProjectName(projectName);
        projectDetailResponseDto.setStartDate(startDate);
        projectDetailResponseDto.setEndDate(endDate);
        return projectDetailResponseDto;
    }
    // </editor-fold>
}
