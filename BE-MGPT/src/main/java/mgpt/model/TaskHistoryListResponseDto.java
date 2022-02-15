package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskHistoryListResponseDto {
    private String username;
    private String avatar;
    private String actionDescription;
    private int taskId;
    private Date actionDate;
}
