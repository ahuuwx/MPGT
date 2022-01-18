package mgpt.repository;

import mgpt.dao.Account;
import mgpt.dao.Project;
import mgpt.dao.ProjectOfUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findAllByProjectOfUserListIsIn(List<ProjectOfUser> projectOfUserList);
}
