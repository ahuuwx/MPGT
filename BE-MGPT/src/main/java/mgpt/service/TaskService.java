package mgpt.service;

import mgpt.dao.Account;
import mgpt.dao.Sprint;
import mgpt.dao.Task;
import mgpt.dao.TaskStatus;
import mgpt.model.TaskCreatingRequestDto;
import mgpt.repository.AccountRepository;
import mgpt.repository.SprintRepository;
import mgpt.repository.TaskRepository;
import mgpt.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    SprintRepository sprintRepository;

    public ResponseEntity<?> createNewTask(TaskCreatingRequestDto newTask) throws Exception {
    try{
        Account account= accountRepository.findAccountByUsername(newTask.getCreatorUsername());
        Sprint sprint=sprintRepository.getById(newTask.getSprintId());
        ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
        ZonedDateTime today = ZonedDateTime.now(zoneId);
        TaskStatus taskStatus=new TaskStatus();
        taskStatus.setStatusId(1);
        if(newTask.getTaskName()!=null&&account!=null&&sprint!=null){
            Task task=new Task();
            task.setTaskName(newTask.getTaskName());
            task.setCreatorUsername(account);
            task.setCreateDate(Date.from(today.toInstant()));
            task.setStatusId(taskStatus);
            task.setSprintId(sprint);
            taskRepository.save(task);
        }else{
            throw new Exception(Constant.INVALID_TASKNAME);
        }
        return ResponseEntity.ok(true);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    }


}