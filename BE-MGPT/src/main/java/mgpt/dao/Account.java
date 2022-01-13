package mgpt.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "account")
public class Account implements Serializable {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "is_available")
    private boolean isAvailable;
    @Column(name = "token_notification")
    private String tokenNotification;

    @OneToMany(mappedBy = "creatorUsername")
    private List<Task> taskList;
    @OneToMany(mappedBy = "receiverUsername")
    private List<Notification> notificationList;
    @ManyToMany(mappedBy = "username")
    private List<ProjectOfUser> projectOfUserList;
    @ManyToMany(mappedBy = "username")
    private List<RoleOfUser> roleOfUserList;


}
