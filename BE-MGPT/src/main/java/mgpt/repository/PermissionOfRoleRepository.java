package mgpt.repository;

import mgpt.dao.PermissionOfRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionOfRoleRepository extends JpaRepository<PermissionOfRole, Integer> {
    List<PermissionOfRole> findAllByRoleId_RoleId(int roleId);
}
