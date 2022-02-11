package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskCreatingRequestDto {
    private String taskName;
    private String creatorUsername;
    private int sprintId;

}
