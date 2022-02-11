package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SprintCreatingRequestDto {
    private String sprintName;
    private Date startDate;
    private int duration;
    private int projectId;
}
