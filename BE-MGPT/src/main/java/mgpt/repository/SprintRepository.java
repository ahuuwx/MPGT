package mgpt.repository;

import mgpt.dao.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Integer> {
    List<Sprint> findAllByProjectId_ProjectId(int projectId);

    Sprint findBySprintId(int sprintId);

    Sprint findBySprintNameIsLikeAndProjectId_ProjectId(String sprintName, int projectId);
}
