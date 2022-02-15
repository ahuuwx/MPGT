package mgpt.service;

import mgpt.dao.Account;
import mgpt.dao.Task;
import mgpt.dao.TaskComment;
import mgpt.dao.TaskHistory;
import mgpt.model.*;
import mgpt.repository.AccountRepository;
import mgpt.repository.TaskHistoryRepository;
import mgpt.repository.TaskRepository;
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
public class TaskHistoryService {
    @Autowired
    TaskHistoryRepository taskHistoryRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TaskRepository taskRepository;

    //<editor-fold desc="Create History In Task">
    public Boolean createNewHistoryInTask(TaskHistoryRequestDto newHistory) throws Exception {
        try{
            Account account=accountRepository.findAccountByUsername(newHistory.getUsername());
            Task task=taskRepository.findByTaskId(newHistory.getTaskId());
            ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
            ZonedDateTime today = ZonedDateTime.now(zoneId);
            if(account!=null){
                if(task==null)
                    throw new Exception(Constant.INVALID_TASKID);

                TaskHistory taskHistory=new TaskHistory();
                taskHistory.setUsername(account);
                taskHistory.setActionDate(Date.from(today.toInstant()));
                if(newHistory.getActionType()==1){
                    taskHistory.setActionDescription(account.getUsername()+" created the Issue.");
                }
                if(newHistory.getActionType()==2){
                    taskHistory.setActionDescription(account.getUsername()+" changed the Task's Name: "+newHistory.getWhatHaveBeenChanged());
                }
                if(newHistory.getActionType()==3){
                    taskHistory.setActionDescription(account.getUsername()+" changed the Assignee: "+ newHistory.getWhatHaveBeenChanged());
                }
                if(newHistory.getActionType()==4){
                    taskHistory.setActionDescription(account.getUsername()+" changed the status: "+ newHistory.getWhatHaveBeenChanged());
                }
                taskHistory.setTask(task);
                taskHistoryRepository.save(taskHistory);
                return true;
            }else
            {
                throw new Exception(Constant.INVALID_USERNAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //</editor-fold>

    //<editor-fold desc="View History List">
    public ResponseEntity<?> viewHistoryListInTask(int taskId) throws Exception {
        try {
            Task task = taskRepository.findByTaskId(taskId);
            if (task == null) {
                throw new Exception(Constant.INVALID_TASKID);
            } else {
                List<TaskHistory> taskHistoryList = taskHistoryRepository.findAllByTask_TaskId(taskId);
                List<HistoryDto> historyListResponseDtos = taskHistoryList.stream().map(taskHistory -> taskHistory.convertToHistoryList()).collect(Collectors.toList());
                return ResponseEntity.ok(historyListResponseDtos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
