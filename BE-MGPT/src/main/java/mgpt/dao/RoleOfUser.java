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
@Table(name = "role_of_user")
public class RoleOfUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int roleOfUserId;

    @ManyToMany
    @JoinColumn(name = "username")
    private List<User> username;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role roleId;
}
