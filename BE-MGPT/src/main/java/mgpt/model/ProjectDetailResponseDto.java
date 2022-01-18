package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectDetailResponseDto {
    private int projectId;
    private String projectName;
    private String leaderName;
    private List<String> lecturerName;
    private Date startDate;
    private Date endDate;
    private List<String> memberList;
}
