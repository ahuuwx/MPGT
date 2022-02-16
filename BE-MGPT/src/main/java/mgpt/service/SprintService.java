package mgpt.service;

import mgpt.dao.Project;
import mgpt.dao.Sprint;
import mgpt.dao.Task;
import mgpt.model.SprintCreatingRequestDto;
import mgpt.model.SprintListResponseDto;
import mgpt.model.SprintUpdatingRequestDto;
import mgpt.model.TaskSummaryInSprintResponseDto;
import mgpt.repository.ProjectRepository;
import mgpt.repository.SprintRepository;
import mgpt.repository.TaskRepository;
import mgpt.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SprintService {
    @Autowired
    SprintRepository sprintRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    ProjectRepository projectRepository;
    SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_PATTERN);

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

    //<editor-fold desc="Convert to Date End">
    public Date convertToTimeEnd(Date timeStart, int duration) throws ParseException {
        Date timeEnd;

        //Date d = sdf.parse(timeStart);
        Calendar cal = Calendar.getInstance();
        cal.setTime(timeStart);
        cal.add(Calendar.DATE, duration);

        return timeEnd = cal.getTime();
    }
    //</editor-fold>

    //<editor-fold desc="Create New Sprint">
    public ResponseEntity<?> createNewSprint(int projectId) throws Exception {
        try {
            Project project = projectRepository.findProjectsByProjectId(projectId);
            if (project != null) {
                Sprint sprint = new Sprint();
                List<Sprint> sprintList=sprintRepository.findAllByProjectId_ProjectId(projectId);
                int countSprint=sprintList.size()+1;
                sprint.setSprintName(project.getProjectName()+" Sprint "+countSprint);
                sprint.setProjectId(project);
                sprintRepository.save(sprint);
                return ResponseEntity.ok(true);
            } else {
                throw new Exception(Constant.INVALID_PROJECT_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Update Sprint">
    public ResponseEntity<?> updateSprint(int sprintId, SprintUpdatingRequestDto updateSprint) throws Exception {
        try{
            Sprint sprint=sprintRepository.findBySprintId(sprintId);
            if(updateSprint.getSprintName()==null||updateSprint.getSprintName().matches(""))
                throw new Exception(Constant.NULL_SPRINT_NAME);
            if(sprint!=null){
                sprint.setSprintName(updateSprint.getSprintName());
                sprint.setStartDate(updateSprint.getStartDate());
                sprint.setEndDate(convertToTimeEnd(updateSprint.getStartDate(),updateSprint.getDuration()));
                sprintRepository.save(sprint);
                return ResponseEntity.ok(true);
            } else
                throw new Exception(Constant.INVALID_SPRINT);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Delete Sprint by Sprint Id">
    public ResponseEntity<?> deleteSprintBySprintId(int sprintId) throws Exception {
        try {

            if (!sprintRepository.existsById(sprintId)) {
                throw new IllegalArgumentException(Constant.INVALID_TASKID);
            } else {
                Sprint delSprint = sprintRepository.findBySprintId(sprintId);
                List<Task> taskList = taskRepository.findAllBySprintId_SprintId(sprintId);
                for (Task task : taskList) {
                    task.setSprintId(null);
                }
                sprintRepository.delete(delSprint);
                return ResponseEntity.ok(Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
