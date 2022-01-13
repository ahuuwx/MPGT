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
@Table(name = "task")
public class Task {
    @Id
    @Column(name = "task_id")
    private int taskId;
    @Column(name = "task_name")
    private String taskName;
    @Column(name = "task_description")
    private String taskDescription;

    @ManyToOne
    @JoinColumn(name = "creator_username")
    private User creatorUsername;

    @ManyToOne
    @JoinColumn(name = "assignee_username")
    private User assigneeUsername;

    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;

    @OneToOne
    @Column(name = "status_id")
    private TaskStatus statusId;
    @ManyToOne
    @Column(name = "sprint_id")
    private Sprint sprintId;


}
