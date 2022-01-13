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
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "role_id")
    private int roleId;
    @Column(name = "role_name")
    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<RoleOfUser> roleOfUserList;

}
