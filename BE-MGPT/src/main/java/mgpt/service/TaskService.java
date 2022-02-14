package mgpt.service;

import mgpt.dao.Account;
import mgpt.dao.Sprint;
import mgpt.dao.Task;
import mgpt.dao.TaskStatus;
import mgpt.model.TaskCreatingRequestDto;
import mgpt.model.TaskDetailResponseDto;
import mgpt.model.TaskResponseBySprintAndStatus;
import mgpt.model.TaskUpdateRequestDto;
import mgpt.repository.AccountRepository;
import mgpt.repository.SprintRepository;
import mgpt.repository.TaskRepository;
import mgpt.repository.TaskStatusRepository;
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
    //local time


    //<editor-fold desc="Create New Task">
    public ResponseEntity<?> createNewTask(TaskCreatingRequestDto newTask) throws Exception {
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
            if (sprint == null) {
                throw new Exception(Constant.INVALID_SPRINT);
            }
            if (newTask.getTaskName() != null) {
                Task task = new Task();
                task.setTaskName(newTask.getTaskName());
                task.setCreatorUsername(account);

                task.setCreateDate(Date.from(today.toInstant()));
                task.setStatusId(taskStatus);
                task.setSprintId(sprint);
                taskRepository.save(task);
            } else {
                throw new Exception(Constant.INVALID_TASKNAME);
            }
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Update Task">
    public ResponseEntity<?> updateTask(int taskId, TaskUpdateRequestDto newTask) throws Exception {
        try {
            Account assigneeAccount = accountRepository.findAccountByUsername(newTask.getAssigneeUsername());
            TaskStatus taskStatus = taskStatusRepository.findByStatusId(newTask.getStatusId());
            Sprint sprint = sprintRepository.findBySprintId(newTask.getSprintId());
            ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
            ZonedDateTime today = ZonedDateTime.now(zoneId);
            if (!taskRepository.existsById(taskId)) {
                throw new IllegalArgumentException(Constant.INVALID_TASKID);
            } else if (assigneeAccount == null) {
                throw new Exception(Constant.INVALID_ASSIGNEEUSERNAME);
            } else if (taskStatus == null) {
                throw new Exception(Constant.INVALID_TASKSTATUS);
            } else if (sprint == null) {
                throw new Exception(Constant.INVALID_SPRINT);
            } else {
                Task updateTask = taskRepository.findByTaskId(taskId);

                updateTask.setTaskName(newTask.getTaskName());
                updateTask.setTaskDescription(newTask.getTaskDescription());

                updateTask.setAssigneeUsername(assigneeAccount);

                updateTask.setUpdateDate(Date.from(today.toInstant()));

                updateTask.setStatusId(taskStatus);

                updateTask.setSprintId(sprint);
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
    public ResponseEntity<?> deleteTaskByTaskId(int taskId) throws Exception {
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
    public ResponseEntity<?> viewTaskDetailByTaskId(int taskId) throws Exception {
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
                return ResponseEntity.ok(taskDetailResponseDto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Vieww Task By Sprint and Status">
    public ResponseEntity<?> viewTaskListBySprintAndStatus(int sprintId, String status) throws Exception {
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