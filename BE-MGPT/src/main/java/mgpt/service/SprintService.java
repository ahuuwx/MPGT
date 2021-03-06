package mgpt.service;

import mgpt.dao.*;
import mgpt.model.*;
import mgpt.repository.ProjectRepository;
import mgpt.repository.SprintRepository;
import mgpt.repository.TaskRepository;
import mgpt.repository.TaskStatusRepository;
import mgpt.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SprintService {
    @Autowired
    SprintRepository sprintRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    TaskStatusRepository taskStatusRepository;


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
        Calendar cal = Calendar.getInstance();
        cal.setTime(timeStart);
        cal.add(Calendar.DATE, duration);

        return timeEnd = cal.getTime();
    }
    //</editor-fold>

    //<editor-fold desc="Create New Sprint">
    public ResponseEntity<?> createNewSprint(int projectId) throws Exception {
        try {
            Project project = projectRepository.findByProjectId(projectId);
            if (project != null) {
                Sprint sprint = new Sprint();
                List<Sprint> sprintList=sprintRepository.findAllByProjectId_ProjectId(projectId);
                int countSprint=sprintList.size();
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
            ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
            ZonedDateTime today = ZonedDateTime.now(zoneId);
            if(updateSprint.getSprintName()==null||updateSprint.getSprintName().matches(""))
                throw new Exception(Constant.NULL_SPRINT_NAME);
            if(updateSprint.getReason()==null)
                throw new Exception(Constant.NULL_UPDATE_SPRINT_REASON);
            if(sprint!=null){
                sprint.setSprintName(updateSprint.getSprintName());
                //n???u ko update ng??y, l??c v???a create xong (date v???n null, t??? ?????ng set ng??y)
                if(updateSprint.getStartDate()==null){
                    sprint.setStartDate(Date.from(today.toInstant()));
                    sprint.setEndDate(convertToTimeEnd(sprint.getStartDate(),7));
                }else {
                    sprint.setStartDate(updateSprint.getStartDate());
                    sprint.setEndDate(convertToTimeEnd(updateSprint.getStartDate(), updateSprint.getDuration()));
                }
                sprint.setReason(updateSprint.getReason());
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
                if(!delSprint.getSprintName().equals("Backlog")) {
                    List<Task> taskList = taskRepository.findAllBySprintId_SprintId(sprintId);
                    String backlog = "Backlog";
                    Sprint backlogSprint = sprintRepository.findBySprintNameIsLikeAndProjectId_ProjectId(backlog, delSprint.getProjectId().getProjectId());
                    for (Task task : taskList) {

                        task.setSprintId(backlogSprint);
                    }
                    sprintRepository.delete(delSprint);
                }else
                    throw new Exception("CAN NOT DELETE SPRINT BACKLOG");
                return ResponseEntity.ok(Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Get Sprint Detail">
    public ResponseEntity<?> getSprintDetail(int sprintId) {
        try {
            Sprint sprint = sprintRepository.findBySprintId(sprintId);
            if (sprint == null) {
                throw new Exception(Constant.INVALID_SPRINT);
            } else {
                SprintReviewResponseDto dto=new SprintReviewResponseDto();
                dto.setSprintName(sprint.getSprintName());
                dto.setFileUrl(sprint.getFileUrl());
                dto.setScore(sprint.getScore());
                //t??nh ph???n tr??m t???ng task c?? trong taskstatus c?? trong sprint
                Map<String, String> map=new HashMap<String,String>();
                //v?? d??? c?? 3 task status
                List<TaskStatus> taskStatusList=taskStatusRepository.findAll();
                //t???t c??? tasks c?? trong sprint ?????u trong list n??y
                List<Task> taskAllStatusList=taskRepository.findAllBySprintId_SprintIdAndStatusIdIsIn(sprintId,taskStatusList);
                //get size of task with all status (big size)
                double size=taskAllStatusList.size();
                int numberOfTask= (int) size;
                dto.setNumberOfTask(numberOfTask);
                //v???i m???i status, l???p l???i v??ng for bao nhi???u l???n,
                // t??m theo t???ng status v??ng for theo status c?? trong list status
                //v?? d??? c?? 3 status s??? l???p l???i ba l???n v?? get list t???ng tasks v???i t???ng status
                if(numberOfTask!=0) {
                    //n???u t???ng size task kh??c 0 th?? v??o for, n???u ch??a c?? task th?? t??nh chi
                    for (TaskStatus taskStatus : taskStatusList) {
                        List<Task> tasksCount = taskRepository.findAllBySprintId_SprintIdAndStatusId_StatusName(sprintId, taskStatus.getStatusName());
                        double sizeInnerTaskList = tasksCount.size();
                        double percent = (sizeInnerTaskList / size);
                        //h??m put trong hashmap bao g???m key v?? value, m???i key s??? c?? value ri??ng
                        //n???u nh???p v??o key tr??ng s??? l???y value sau, value tr?????c s??? ??c tr??? v???
                        NumberFormat numEN = NumberFormat.getPercentInstance();
                        String percentageEN = numEN.format(percent);
                        map.put(taskStatus.getStatusName(), percentageEN);

                    }
                    dto.setPercent(map);
                }
                else
                    dto.setPercent(null);


                return ResponseEntity.ok(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Review Sprint By Lecturer">
    public ResponseEntity<?> reviewSprintByLecturer(int sprintId, float score) throws Exception {
        try {
            Sprint sprint = sprintRepository.findBySprintId(sprintId);
            if (sprint == null)
                throw new Exception(Constant.INVALID_SPRINT);
            else {
                sprint.setScore(score);
                sprintRepository.save(sprint);
                return ResponseEntity.ok(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="update sprint review, set fireUrl to null">
    public ResponseEntity<?> updateSprintReviewBySprintId(int sprintId) throws Exception {
        try{
            Sprint sprint=sprintRepository.findBySprintId(sprintId);
            if(sprint!=null){
                sprint.setFileUrl(null);
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
}

