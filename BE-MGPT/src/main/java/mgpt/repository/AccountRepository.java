package mgpt.repository;

import mgpt.dao.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account findAccountByUsername(String username);

    Account findAccountByRoleOfUser_RoleId_RoleIdAndProjectOfUser_ProjectOfUserId(int roleId, int projectOfUserId);

}
