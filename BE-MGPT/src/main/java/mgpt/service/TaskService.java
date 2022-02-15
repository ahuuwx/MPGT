package mgpt.service;

import mgpt.dao.*;
import mgpt.model.*;
import mgpt.repository.*;
import mgpt.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    SprintRepository sprintRepository;
    @Autowired
    TaskStatusRepository taskStatusRepository;
    @Autowired
    TaskHistoryService taskHistoryService;
    @Autowired
    TaskCommentRepository taskCommentRepository;
    @Autowired
    TaskHistoryRepository taskHistoryRepository;

    //<editor-fold desc="Create New Task">
    public ResponseEntity<?> createNewTask(TaskCreatingRequestDto newTask) {
        try {
            Account account = accountRepository.findAccountByUsername(newTask.getCreatorUsername());
            Sprint sprint = sprintRepository.findBySprintId(newTask.getSprintId());

            TaskStatus taskStatus = new TaskStatus();
            taskStatus.setStatusId(1);
            ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
            ZonedDateTime today = ZonedDateTime.now(zoneId);
            if (account == null) {
                throw new Exception(Constant.INVALID_USERNAME);
            }
            //check if sprint ID is invalid and not match null ( 0 )
            if (sprint == null && newTask.getSprintId() != 0) {
                throw new Exception(Constant.INVALID_SPRINT);
            }
            if (newTask.getTaskName() != null) {
                Task task = new Task();
                task.setTaskName(newTask.getTaskName());
                task.setCreatorUsername(account);

                task.setCreateDate(Date.from(today.toInstant()));
                task.setStatusId(taskStatus);
                //if sprint null
                if (newTask.getSprintId() == 0) {
                    task.setSprintId(null);
                } else
                    task.setSprintId(sprint);

                taskRepository.save(task);
                //create history in Create Task
                TaskHistoryRequestDto historyRequestDto = new TaskHistoryRequestDto();
                historyRequestDto.setUsername(account.getUsername());
                historyRequestDto.setActionType(1);
                historyRequestDto.setTaskId(task.getTaskId());
                Boolean check = taskHistoryService.createNewHistoryInTask(historyRequestDto);
                if (!check)
                    throw new Exception("Create History in task Failed.");
                //
                return ResponseEntity.ok(true);
            } else {
                throw new Exception(Constant.INVALID_TASKNAME);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Update Task">
    public ResponseEntity<?> updateTask(int taskId, TaskUpdateRequestDto newTask) {
        try {
            //assignee, status, sprint lúc nào cũng có vì có api hiện list
            Account assigneeAccount = accountRepository.findAccountByUsername(newTask.getAssigneeUsername());
            TaskStatus taskStatus = taskStatusRepository.findByStatusId(newTask.getStatusId());
            Sprint sprint = sprintRepository.findBySprintId(newTask.getSprintId());
            ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
            ZonedDateTime today = ZonedDateTime.now(zoneId);
            if (!taskRepository.existsById(taskId)) {
                throw new IllegalArgumentException(Constant.INVALID_TASKID);
            } else {
                Task updateTask = taskRepository.findByTaskId(taskId);

                updateTask.setTaskDescription(newTask.getTaskDescription());
                updateTask.setUpdateDate(Date.from(today.toInstant()));


                //<editor-fold desc="Create History In Update Task">
                TaskHistoryRequestDto historyRequestDto = new TaskHistoryRequestDto();
                historyRequestDto.setUsername(newTask.getActorUsername());
                historyRequestDto.setTaskId(taskId);

                /**
                 * Get action Type
                 */
                //change Task Name
                String s1=updateTask.getTaskName();
                String s2= newTask.getTaskName();
                if (!s1.equals(s2)) {
                    historyRequestDto.setActionType(2);
                    historyRequestDto.setWhatHaveBeenChanged(updateTask.getTaskName() + " -> " + newTask.getTaskName());
                    Boolean check = taskHistoryService.createNewHistoryInTask(historyRequestDto);

                    if (!check)
                        throw new Exception("Create History in task Failed.");
                }
                //change assignee
                String s3=updateTask.getAssigneeUsername().getUsername();
                String s4=newTask.getAssigneeUsername();
                if (!s3.equals(s4)) {
                    historyRequestDto.setActionType(3);
                    historyRequestDto.setWhatHaveBeenChanged(updateTask.getAssigneeUsername().getUsername() + " -> " + newTask.getAssigneeUsername());
                    Boolean check = taskHistoryService.createNewHistoryInTask(historyRequestDto);
                    if (!check)
                        throw new Exception("Create History in task Failed.");
                }
                //change status
                int t1=updateTask.getStatusId().getStatusId();
                int t2= newTask.getStatusId();
                if (t1 != t2) {
                    historyRequestDto.setActionType(4);
                    historyRequestDto.setWhatHaveBeenChanged(updateTask.getStatusId().getStatusName() + " -> " + taskStatus.getStatusName());
                    //store history
                    Boolean check = taskHistoryService.createNewHistoryInTask(historyRequestDto);
                    if (!check)
                        throw new Exception("Create History in task Failed.");
                }
                /**
                 *
                 */
                //</editor-fold>
                if (assigneeAccount == null)
                    updateTask.setAssigneeUsername(null);
                else
                    updateTask.setAssigneeUsername(assigneeAccount);
                if (taskStatus == null)
                    updateTask.setStatusId(null);
                else
                    updateTask.setStatusId(taskStatus);
                if (sprint == null)
                    updateTask.setSprintId(null);
                else
                    updateTask.setSprintId(sprint);

                updateTask.setTaskName(newTask.getTaskName());
                taskRepository.save(updateTask);
                return ResponseEntity.ok(Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Delete Task by Task Id">
    public ResponseEntity<?> deleteTaskByTaskId(int taskId) {
        try {
            if (!taskRepository.existsById(taskId)) {
                throw new IllegalArgumentException(Constant.INVALID_TASKID);
            } else {
                Task delTask = taskRepository.findByTaskId(taskId);
                taskRepository.delete(delTask);
                return ResponseEntity.ok(Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="View Task Detail">
    public ResponseEntity<?> viewTaskDetailByTaskId(int taskId) {
        try {
            Task task = taskRepository.findByTaskId(taskId);
            if (task == null) {
                throw new Exception(Constant.INVALID_TASKID);
            } else {
                TaskDetailResponseDto taskDetailResponseDto = new TaskDetailResponseDto();
                taskDetailResponseDto.setTaskId(task.getTaskId());
                taskDetailResponseDto.setTaskName(task.getTaskName());
                taskDetailResponseDto.setTaskDescription(task.getTaskDescription());
                taskDetailResponseDto.setCreatorUser(task.getCreatorUsername().converToAccountSummaryDto());
                if (task.getAssigneeUsername() == null) {
                    taskDetailResponseDto.setAssigneeUser(null);
                } else
                    taskDetailResponseDto.setAssigneeUser(task.getAssigneeUsername().converToAccountSummaryDto());
                taskDetailResponseDto.setCreateDate(task.getCreateDate());
                taskDetailResponseDto.setUpdateDate(task.getUpdateDate());
                taskDetailResponseDto.setStatusId(task.getStatusId().getStatusId());
                taskDetailResponseDto.setSprint(task.getSprintId().convertToSprintDto());
                //Comment
                List<TaskComment> taskCommentList=taskCommentRepository.findAllByTask_TaskId(taskId);
                List<CommentDto> commentDtos=taskCommentList.stream().map(taskComment -> taskComment.convertToTaskCommentListDto()).collect(Collectors.toList());
                taskDetailResponseDto.setComment(commentDtos);
                //History
                List<TaskHistory> taskHistoryList = taskHistoryRepository.findAllByTask_TaskId(taskId);
                List<HistoryDto> historyListResponseDtos = taskHistoryList.stream().map(taskHistory -> taskHistory.convertToHistoryList()).collect(Collectors.toList());
                taskDetailResponseDto.setHistory(historyListResponseDtos);

                return ResponseEntity.ok(taskDetailResponseDto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="View Task By Sprint and Status">
    public ResponseEntity<?> viewTaskListBySprintAndStatus(int sprintId, String status) {
        try {
            Sprint sprint = sprintRepository.findBySprintId(sprintId);
            if (sprint == null) {
                throw new Exception(Constant.INVALID_SPRINT);
            } else {
                List<Task> taskList = taskRepository.findAllBySprintId_SprintIdAndStatusId_StatusName(sprintId, status);
                List<TaskResponseBySprintAndStatus> taskResponseList = taskList.stream().map(task -> task.convertToTaskResponeBySprintAndStatusDto()).collect(Collectors.toList());
                return ResponseEntity.ok(taskResponseList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

}