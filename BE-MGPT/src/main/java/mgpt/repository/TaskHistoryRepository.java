package mgpt.repository;

import mgpt.dao.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistory,Integer> {
    List<TaskHistory>findAllByTask_TaskId(int taskId);
}
