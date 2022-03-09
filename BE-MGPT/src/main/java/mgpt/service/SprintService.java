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
            ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
            ZonedDateTime today = ZonedDateTime.now(zoneId);
            if(updateSprint.getSprintName()==null||updateSprint.getSprintName().matches(""))
                throw new Exception(Constant.NULL_SPRINT_NAME);
            if(sprint!=null){
                sprint.setSprintName(updateSprint.getSprintName());
                //nếu ko update ngày, lúc vừa create xong (date vẫn null, tự động set ngày)
                if(updateSprint.getStartDate()==null){
                    sprint.setStartDate(Date.from(today.toInstant()));
                    sprint.setEndDate(convertToTimeEnd(sprint.getStartDate(),7));
                }else {
                    sprint.setStartDate(updateSprint.getStartDate());
                    sprint.setEndDate(convertToTimeEnd(updateSprint.getStartDate(), updateSprint.getDuration()));
                }
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
                String backlog="Backlog";
                Sprint backlogSprint=sprintRepository.findBySprintNameIsLike(backlog);
                for (Task task : taskList) {

                    task.setSprintId(backlogSprint);
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
                //tính phần trăm từng task có trong taskstatus có trong sprint
                Map<String, String> map=new HashMap<String,String>();
                //ví dụ có 3 task status
                List<TaskStatus> taskStatusList=taskStatusRepository.findAll();
                //tất cả tasks có trong sprint đều trong list này
                List<Task> taskAllStatusList=taskRepository.findAllBySprintId_SprintIdAndStatusIdIsIn(sprintId,taskStatusList);
                //get size of task with all status (big size)
                double size=taskAllStatusList.size();
                int numberOfTask= (int) size;
                dto.setNumberOfTask(numberOfTask);
                //với mỗi status, lặp lại vòng for bao nhiều lần,
                // tìm theo từng status vòng for theo status có trong list status
                //ví dụ có 3 status sẽ lặp lại ba lần và get list từng tasks với từng status
                if(numberOfTask!=0) {
                    //nếu tổng size task khác 0 thì vào for, nếu chưa có task thì tính chi
                    for (TaskStatus taskStatus : taskStatusList) {
                        List<Task> tasksCount = taskRepository.findAllBySprintId_SprintIdAndStatusId_StatusName(sprintId, taskStatus.getStatusName());
                        double sizeInnerTaskList = tasksCount.size();
                        double percent = (sizeInnerTaskList / size);
                        //hàm put trong hashmap bao gồm key và value, mỗi key sẽ có value riêng
                        //nếu nhập vào key trùng sẽ lấy value sau, value trước sẽ đc trả về
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
}
