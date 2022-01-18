package mgpt.service;

import mgpt.dao.Sprint;
import mgpt.dao.Task;
import mgpt.model.SprintListResponseDto;
import mgpt.model.TaskSummaryInSprintResponseDto;
import mgpt.repository.SprintRepository;
import mgpt.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SprintService {
    @Autowired
    SprintRepository sprintRepository;
    @Autowired
    TaskRepository taskRepository;

    //<editor-fold desc="Get Sprint List By Project">
    public ResponseEntity<?> getSprintsByProject(int projectId) {
        try {
            List<Sprint> sprintList = sprintRepository.findAllByProjectId_ProjectId(projectId);
            List<SprintListResponseDto> sprintListResponseDtos = sprintList.stream().map(sprint -> sprint.convertToSprintListDto()).collect(Collectors.toList());

            for (SprintListResponseDto sprintListResponseDto : sprintListResponseDtos) {
                List<Task> tasks = taskRepository.findAllBySprintId_SprintId(sprintListResponseDto.getSprintId());
                List<TaskSummaryInSprintResponseDto> taskList = tasks.stream().map(task -> task.convertToTaskSummaryInSprintDto()).collect(Collectors.toList());
                sprintListResponseDto.setTaskList(taskList);
            }

            return ResponseEntity.ok(sprintListResponseDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

}
