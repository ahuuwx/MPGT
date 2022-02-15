package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskHistoryRequestDto {
    private String username;
    private String actionDescription;
    private int taskId;
    private int actionType;
    /**
     * 1: create the issue
     * 2: update status
     * 3: change assignee
     */
    private String whatHaveBeenChanged;

}
