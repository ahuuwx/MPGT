package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HistoryDto {
    private int taskHistoryId;
    private String actionDescription;
    private Date actionDate;
    private String username;
    private String name;
}
