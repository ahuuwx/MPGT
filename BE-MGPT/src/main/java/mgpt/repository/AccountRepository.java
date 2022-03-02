package mgpt.repository;

import mgpt.dao.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account findAccountByUsername(String username);

    @Query(
            value = "select a from Account as a " +
                    "join ProjectOfUser pou on a.username=pou.username.username " +
                    "join Project p on pou.project.projectId=p.projectId " +
                    "where a.role.roleId=:roleId and " +
                    "p.projectId=:projectId"
    )
    Account findAccountByRole_RoleIdAndProjectOfUser_Project_ProjectId(int roleId, int projectId);

    @Query(
            value = "select a from Account as a " +
                    "join ProjectOfUser pou on a.username=pou.username.username " +
                    "join Project p on pou.project.projectId=p.projectId " +
                    "where a.role.roleId=:roleId and " +
                    "p.projectId=:projectId"
    )
    List<Account> findDistinctByRole_RoleIdAndProjectOfUser_Project_ProjectId(int roleId, int projectId);

}
