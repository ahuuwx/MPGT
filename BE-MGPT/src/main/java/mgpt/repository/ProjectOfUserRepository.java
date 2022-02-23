package mgpt.repository;


import mgpt.dao.Account;
import mgpt.dao.ProjectOfUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProjectOfUserRepository extends JpaRepository<ProjectOfUser, Integer> {
    ProjectOfUser findProjectOfUserByUsername(Account account);

    //xét từng start date với sd và ed với ed
    List<ProjectOfUser> findProjectOfUserByUsername_UsernameAndProject_StartDateLessThanEqualAndProject_EndDateGreaterThanEqual(String username, Date startDate, Date endDate);
    //xét giữa sd và khoảng thời gian ng dùng tìm kiếm có prj nào start ko
    List<ProjectOfUser> findProjectOfUserByUsername_UsernameAndProject_StartDateBetween(String username, Date startDate, Date endDate);

    List<ProjectOfUser> findProjectOfUserByUsername_Username(String username);

}
