package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectListResponseDto {
    private int projectId;
    private String projectName;
    private String leaderName;
    private int semester;
    private List<String> lecturerName;
}
