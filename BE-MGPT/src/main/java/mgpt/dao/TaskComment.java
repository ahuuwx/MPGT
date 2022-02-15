package mgpt.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mgpt.model.TaskCommentListResponseDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "task_comment")
public class TaskComment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_comment_id")
    private int taskCommentId;
    @Column(name = "comment")
    private String comment;
    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "username")
    private Account Username;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskCommentListResponseDto convertToTaskCommentListDto(){
        TaskCommentListResponseDto dto=new TaskCommentListResponseDto();
        dto.setUsername(Username.getUsername());
        dto.setComment(comment);
        dto.setAvatar(getUsername().getAvatar());
        return dto;
    }


}
