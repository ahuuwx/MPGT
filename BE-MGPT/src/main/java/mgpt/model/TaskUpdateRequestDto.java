package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskUpdateRequestDto {
    private String taskName;
    private String taskDescription;
    private String assigneeUsername;
    private String actorUsername;
    private int sprintId;
    private int statusId;   
}
