package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskUpdateRequestDto {
    private String taskName;
    private String taskDescription;
    private String assigneeUsername;
    private int sprintId;
    private int statusId;
}
