package mgpt.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mgpt.model.AccountSummaryDto;

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
    @Column(name = "avatar")
    private String avatar;

    @OneToMany(mappedBy = "creatorUsername")
    private List<Task> taskList;
    @OneToMany(mappedBy = "receiverUsername")
    private List<Notification> notificationList;

    @OneToMany(mappedBy = "username")
    private List<ProjectOfUser> projectOfUserList;

    @OneToOne(mappedBy = "account")
    private RoleOfUser roleOfUser;

    public AccountSummaryDto converToAccountSummaryDto() {
        AccountSummaryDto accountSummaryDto = new AccountSummaryDto();
        accountSummaryDto.setUsername(username);
        accountSummaryDto.setName(name);
        accountSummaryDto.setAvatar(avatar);
        return accountSummaryDto;
    }


}
