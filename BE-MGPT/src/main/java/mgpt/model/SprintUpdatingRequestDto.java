package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SprintUpdatingRequestDto {
    private String sprintName;
    private Date startDate;
    private int duration;
    private String reason;
}
