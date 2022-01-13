package mgpt.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;
    @Column(name = "title")
    private String title;
    @Column(name = "body")
    private String body;
    @Column(name = "sender_username")
    private String senderUsername;
    @Column(name = "is_read")
    private boolean isRead;
    @Column(name = "creating_date")
    private Date creatingDate;

    @ManyToOne
    @JoinColumn(name = "receiver_username")
    private User receiverUsername;
}
