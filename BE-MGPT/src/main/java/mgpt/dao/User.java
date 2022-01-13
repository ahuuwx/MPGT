package mgpt.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User {
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

    @OneToMany(mappedBy = "username")
    private List<Task> taskList;
    @OneToMany(mappedBy = "username")
    private List<Notification> notificationList;
    @ManyToMany(mappedBy = "username")
    private List<ProjectOfUser> projectOfUserList;
    @ManyToMany(mappedBy = "username")
    private List<RoleOfUser> roleOfUserList;


}
