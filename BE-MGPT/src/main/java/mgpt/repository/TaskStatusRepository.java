package mgpt.repository;

import mgpt.dao.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus,Integer> {
    TaskStatus findByStatusId(int statusId);
}
