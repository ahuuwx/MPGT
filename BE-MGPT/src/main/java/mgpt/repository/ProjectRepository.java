package mgpt.repository;

import mgpt.dao.Project;
import mgpt.dao.ProjectOfUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query(
            value = "select p " +
                    "from Project as p" +
                    " join ProjectOfUser po on p.projectId=po.project.projectId" +
                    " where po.project.projectId=:projectId"
    )
    Project findProjectByProjectOfUser_ProjectId(int projectId);

    List<Project> findProjectsByProjectOfUserListIsIn(List<ProjectOfUser> projectOfUserList);

    Project findByProjectId(int projectId);


}
