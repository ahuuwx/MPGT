package mgpt.dao;

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
@Table(name = "sprint")
public class Sprint {
    @Id
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

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project projectId;

}
