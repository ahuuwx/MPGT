package mgpt.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mgpt.model.TaskSummaryInSprintResponseDto;

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
    private Account creatorUsername;

    @ManyToOne
    @JoinColumn(name = "assignee_username")
    private Account assigneeUsername;

    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;

    @OneToOne
    @JoinColumn(name = "status_id")
    private TaskStatus statusId;
    @ManyToOne
    @JoinColumn(name = "sprint_id")
    private Sprint sprintId;

    //<editor-fold desc="convertToTaskSummaryInSprintDto">
    public TaskSummaryInSprintResponseDto convertToTaskSummaryInSprintDto() {
        TaskSummaryInSprintResponseDto taskSummaryInSprintResponseDto = new TaskSummaryInSprintResponseDto();
        taskSummaryInSprintResponseDto.setTaskId(taskId);
        taskSummaryInSprintResponseDto.setTaskName(taskName);
        taskSummaryInSprintResponseDto.setStatusId(statusId.getStatusId());
        taskSummaryInSprintResponseDto.setAssigneeUsername(assigneeUsername.getUsername());
        taskSummaryInSprintResponseDto.setAssigneeName(assigneeUsername.getName());
        return taskSummaryInSprintResponseDto;
    }
    //</editor-fold>


}
