package mgpt.repository;

import mgpt.dao.Task;
import mgpt.dao.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllBySprintId_SprintId(int sprintId);

    Task findByTaskId(int taskId);

    List<Task> findAllBySprintId_SprintIdAndStatusId_StatusName(int sprintId, String statusName);

    List<Task> findAllBySprintId_SprintIdAndStatusIdIsIn(int sprintId, List<TaskStatus> statusList);

}
