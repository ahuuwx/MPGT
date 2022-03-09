package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SprintReviewResponseDto {
    private String sprintName;
    private String fileUrl;
    private int numberOfTask;
    private Map<String, String> percent;
    private float score;
}
