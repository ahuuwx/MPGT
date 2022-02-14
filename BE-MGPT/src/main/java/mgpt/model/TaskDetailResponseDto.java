package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskDetailResponseDto {
    private int taskId;
    private String taskName;
    private String taskDescription;
    private AccountSummaryDto creatorUser;
    private AccountSummaryDto assigneeUser;
    private Date createDate;
    private Date updateDate;
    private int statusId;
    private SprintDto sprint;

}
