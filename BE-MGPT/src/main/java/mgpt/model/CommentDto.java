package mgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDto {
    private int taskCommentId;
    private String username;
    private String avatar;
    private String name;
    private String comment;
    private Date createDate;
}
