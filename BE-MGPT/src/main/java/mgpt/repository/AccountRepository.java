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
                    "join RoleOfUser rou on a.roleOfUser.roleId.roleId=rou.roleId.roleId " +
                    "join ProjectOfUser pou on a.username=pou.username.username " +
                    "join Project p on pou.project.projectId=p.projectId " +
                    "where rou.roleId.roleId=:roleId and " +
                    "p.projectId=:projectId"
    )
    Account findAccountByRoleOfUser_RoleId_RoleIdAndProjectOfUser_Project_ProjectId(int roleId, int projectId);

    @Query(
            value = "select a from Account as a " +
                    "join RoleOfUser rou on a.username=rou.account.username " +
                    "join ProjectOfUser pou on a.username=pou.username.username " +
                    "join Project p on pou.project.projectId=p.projectId " +
                    "where rou.roleId.roleId=:roleId and " +
                    "p.projectId=:projectId"
    )
    List<Account> findDistinctByRoleOfUser_RoleId_RoleIdAndProjectOfUser_Project_ProjectId(int roleId, int projectId);
}
