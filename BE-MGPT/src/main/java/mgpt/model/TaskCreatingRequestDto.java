package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskCreatingRequestDto {
    private String taskName;
    private String creatorUsername;
    private Date createDate;
    private int sprintId;

}
