package mgpt.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mgpt.model.HistoryDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "task_history")
public class TaskHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_history_id")
    private int taskHistoryId;
    @Column(name = "action_description")
    private String actionDescription;
    @Column(name = "action_date")
    private Date actionDate;

    @ManyToOne
    @JoinColumn(name = "username")
    private Account Username;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public HistoryDto convertToHistoryList() {
        HistoryDto dto = new HistoryDto();
        dto.setTaskHistoryId(taskHistoryId);
        dto.setActionDescription(actionDescription);
        dto.setActionDate(actionDate);
        dto.setUsername(Username.getUsername());
        dto.setName(Username.getName());
        return dto;
    }
}
