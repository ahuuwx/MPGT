package mgpt.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "task_status")
public class TaskStatus {
    @Id
    @Column(name = "status_id")
    private int statusId;
    @Column(name = "status_name")
    private String statusName;

}
