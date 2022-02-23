package mgpt.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mgpt.model.PermissionDto;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "permission_of_role")
public class PermissionOfRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @OneToOne
    @JoinColumn(name="role_id")
    private Role roleId;

    public PermissionDto convertToDto(){
        PermissionDto permissionDto=new PermissionDto();
        permissionDto.setPermission(permission.getPermission());
        permissionDto.setRoleName(roleId.getRoleName());
        permissionDto.setTrue(true);
        return  permissionDto;
    }

}
