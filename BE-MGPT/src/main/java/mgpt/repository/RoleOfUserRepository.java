package mgpt.repository;

import mgpt.dao.RoleOfUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleOfUserRepository extends JpaRepository<RoleOfUser, Integer> {
    RoleOfUser findRoleOfUserByAccount_Username(String username);

}
