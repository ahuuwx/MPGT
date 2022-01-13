package mgpt.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name="start_date")
    private Date startDate;
    @Column(name="end_date")
    private Date endDate;

    @OneToMany(mappedBy = "projectId")
    @JsonIgnore
    private List<Sprint> sprintList;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private List<Meeting> meetingList;
}
