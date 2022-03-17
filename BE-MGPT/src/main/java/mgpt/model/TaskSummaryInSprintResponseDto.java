package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskSummaryInSprintResponseDto {
    private int taskId;
    private String taskName;
    private int statusId;
    private String assigneeUsername;
    private String avatar;
    private String assigneeName;
}
