package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SprintListResponseDto {
    private int sprintId;
    private String sprintName;
    private Date startDate;
    private Date endDate;
    private String fileUrl;
    private float score;
    private String reason;
    private List<TaskSummaryInSprintResponseDto> taskList;
}
