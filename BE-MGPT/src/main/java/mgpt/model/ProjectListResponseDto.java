package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectListResponseDto {
    private int projectId;
    private String projectName;
    private String leaderName;
    private int semester;
    private Date startDate;
    private Date endDate;
    private List<String> lecturerName;
}
