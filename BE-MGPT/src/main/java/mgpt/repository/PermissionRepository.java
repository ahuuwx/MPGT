package mgpt.repository;

import mgpt.dao.Permission;
import mgpt.dao.PermissionOfRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Integer> {
}
