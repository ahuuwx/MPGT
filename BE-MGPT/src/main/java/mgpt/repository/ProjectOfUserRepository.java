package mgpt.repository;


import mgpt.dao.Account;
import mgpt.dao.ProjectOfUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectOfUserRepository extends JpaRepository<ProjectOfUser, Integer> {
    ProjectOfUser findProjectOfUserByUsername(Account account);

    List<ProjectOfUser> findProjectOfUserByUsername_Username(String username);


}
