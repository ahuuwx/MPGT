package mgpt.service;

import mgpt.dao.Account;
import mgpt.dao.Sprint;
import mgpt.dao.Task;
import mgpt.dao.TaskComment;
import mgpt.model.*;
import mgpt.repository.AccountRepository;
import mgpt.repository.TaskCommentRepository;
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
public class TaskCommentService {
    @Autowired
    TaskCommentRepository taskCommentRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TaskRepository taskRepository;

    //<editor-fold desc="Create New Comment">
    public ResponseEntity<?> createNewCommentInTask(TaskCommentRequestDto newComment) throws Exception {
    try{
        Account account=accountRepository.findAccountByUsername(newComment.getUsername());
        Task task=taskRepository.findByTaskId(newComment.getTaskId());
        ZoneId zoneId = ZoneId.of(Constant.TIMEZONE);
        ZonedDateTime today = ZonedDateTime.now(zoneId);
        if(account!=null){
            if(task==null)
                throw new Exception(Constant.INVALID_TASKID);
            if(newComment.getComment()==null)
                throw new Exception(Constant.NULL_TASK_COMMENT);
            TaskComment taskComment=new TaskComment();
            taskComment.setUsername(account);
            taskComment.setComment(newComment.getComment());
            taskComment.setCreateDate(Date.from(today.toInstant()));
            taskComment.setTask(task);
            taskCommentRepository.save(taskComment);
            return ResponseEntity.ok(true);
        }else
        {
            throw new Exception(Constant.INVALID_USERNAME);
        }
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    }
    //</editor-fold>

    //<editor-fold desc="View Comment List">
    public ResponseEntity<?> viewCommentListInTask(int taskId) throws Exception {
        try {
            Task task = taskRepository.findByTaskId(taskId);
            if (task == null) {
                throw new Exception(Constant.INVALID_TASKID);
            } else {
                List<TaskComment> taskCommentList = taskCommentRepository.findAllByTask_TaskId(taskId);
                List<CommentDto> taskCommentListResponseDtoList = taskCommentList.stream().map(taskComment -> taskComment.convertToTaskCommentListDto()).collect(Collectors.toList());
                return ResponseEntity.ok(taskCommentListResponseDtoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Delete Task by Task Id">
    public ResponseEntity<?> deleteCommentByCommentId(int commentId) {
        try {
            if (!taskCommentRepository.existsById(commentId)) {
                throw new IllegalArgumentException(Constant.INVALID_TASK_COMMENT);
            } else {
                TaskComment delComment = taskCommentRepository.findByTaskCommentId(commentId);
                taskCommentRepository.delete(delComment);
                return ResponseEntity.ok(Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    public ResponseEntity<?> updateComment(int commentId, CommentUpdateRequestDto commentRequestDto) throws Exception {
        try{
            TaskComment taskComment=taskCommentRepository.findByTaskCommentId(commentId);
            if(commentRequestDto.getCommentText()==null||commentRequestDto.getCommentText().matches(""))
                throw new Exception(Constant.NULL_TASK_COMMENT);
            if(taskComment!=null){
                taskComment.setComment(commentRequestDto.getCommentText());
                taskCommentRepository.save(taskComment);
                return ResponseEntity.ok(true);
            } else
                throw new Exception(Constant.INVALID_TASK_COMMENT);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    }
