package mgpt.repository;

import mgpt.dao.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment,Integer> {
    List<TaskComment> findAllByTask_TaskId(int taskId);
}
