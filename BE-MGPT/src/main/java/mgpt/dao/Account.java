package mgpt.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne(mappedBy = "username")
    private ProjectOfUser projectOfUser;

    @OneToOne(mappedBy = "account")
    private RoleOfUser roleOfUser;




}
