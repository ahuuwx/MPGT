package mgpt.controller;

import mgpt.model.*;
import mgpt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@EnableScheduling
public class RestAPI {

    @Autowired
    AccountService accountService;
    @Autowired
    ProjectService projectService;
    @Autowired
    SprintService sprintService;
    @Autowired
    TaskService taskService;
    @Autowired
    TaskCommentService taskCommentService;
    @Autowired
    TaskHistoryService taskHistoryService;
    @Autowired
    PermissionOfRoleService permissionOfRoleService;
    @Autowired
    FireBaseService fireBaseService;
    @Autowired
    MeetingService meetingService;

    /**
     * -------------------------------WELCOME--------------------------------
     */
    //<editor-fold desc="Welcome Page">

    /**
     * @return welcome page
     * @apiNote welcome page
     */
    @CrossOrigin
    @RequestMapping(value = "/")

    public String welcome() {
        return "Welcome to MPGT - Manage the progress of the graduation thesis.!\n" + ZonedDateTime.now();
    }
    //</editor-fold>

    /**
     * -------------------------------ACCOUNT--------------------------------
     */

    //<editor-fold desc="1.01-check-login">

    /**
     * @param
     * @return
     * @throws Exception
     * @apiNote 1.01-check-login
     * @author HuuNt - 2022.01.13
     */
    @CrossOrigin
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity checkLogin(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
        return accountService.checkLogin(loginRequestDto);
    }
    //</editor-fold>

    //<editor-fold desc="Get member In project">

    /**
     * @param projectId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/member-list", method = RequestMethod.GET)
    public ResponseEntity<?> getMemBerInProject(@RequestParam int projectId) throws Exception {
        return accountService.getMemBerInProject(projectId);
    }
    //</editor-fold>

    /**
     * -------------------------------PROFILE--------------------------------
     */

    //<editor-fold desc="Get profile">

    /**
     *
     * @param username
     * @return
     * @throws Exception
     * @apiNote get profile
     * @author HuuNt - 2022.01.14
     */
    @CrossOrigin
    @RequestMapping(value = "/get-profile", method = RequestMethod.GET)
    public ResponseEntity<?> getProfileByUsername(@RequestParam(value = "username") String username) throws Exception {
        return accountService.getProfileByUsername(username);
    }
    //</editor-fold>

    /**
     * -------------------------------PROJECT--------------------------------
     */

    //<editor-fold desc="1.3 Get Project List">

    /**
     *
     * @param username
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public ResponseEntity<?> getProjectsByUsername(@RequestParam(value = "username") String username) throws Exception {
        return projectService.getProjectsByUsername(username);
    }
    //</editor-fold>

    //<editor-fold desc="1.4 Get Project Detail">
    /**
     *
     * @param projectId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/project", params = "projectId",method = RequestMethod.GET)
    public ResponseEntity<?> getProjectDetailByProjectId(@RequestParam(value = "projectId") int projectId) throws Exception {
        return projectService.getProjectDetailByProjectId(projectId);
    }
    //</editor-fold>

    //<editor-fold desc="Get Project By Date">

    /**
     *
     * @param dto
     * @param username
     * @return
     * @throws Exception
     */
    @CrossOrigin
        @RequestMapping(value = "/project", method = RequestMethod.GET)
        public ResponseEntity<?> getProjectByDate(@RequestBody ProjectListSearchByDateDto dto,
                                                  @RequestParam(value = "username") String username) throws Exception {
            return projectService.getProjectByDate(dto,username);
        }
    //</editor-fold>

    /**
     * -------------------------------SPRINT--------------------------------
     */

    //<editor-fold desc="Create New Sprint">

    /**
     *
     * @param projectId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/sprint", method = RequestMethod.POST)
    public ResponseEntity<?> createNewSprint(@RequestParam int projectId) throws Exception {
        return sprintService.createNewSprint(projectId);
    }
    //</editor-fold>

    //<editor-fold desc="Delete Sprint">

    /**
     * @param sprintId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/sprint", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSprintBySprintId(@RequestParam int sprintId) throws Exception {
        return sprintService.deleteSprintBySprintId(sprintId);
    }
    //</editor-fold>

    //<editor-fold desc="Update Sprint">
    /**
     * @param sprintId
     * @param updateSprint
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/sprint", method = RequestMethod.PUT)
    public ResponseEntity<?> updateSprintBySprintId(@RequestParam int sprintId,
                                                    @RequestBody SprintUpdatingRequestDto updateSprint) throws Exception {
        return sprintService.updateSprint(sprintId, updateSprint);
    }
    //</editor-fold>

    //<editor-fold desc="View Sprint List">

    /**
     * @param projectId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/sprints", method = RequestMethod.GET)
    public ResponseEntity<?> getSprintsByProject(@RequestParam(value = "projectId") int projectId) throws Exception {
        return sprintService.getSprintsByProject(projectId);
    }
    //</editor-fold>

    //<editor-fold desc="Upload File File In Sprint By Leader">
    /**
     *
     * @param file
     * @param sprintId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/leader-review-sprint", method = RequestMethod.POST)

    public ResponseEntity<?> uploadFileInSprintByLeader(@RequestParam(value = "file") List<MultipartFile> file,
                                    @RequestParam int sprintId) throws Exception {
        return fireBaseService.uploadToThisMachineInSprint(file, sprintId);
    }
    //</editor-fold>

    //<editor-fold desc="Get Sprint Review Detail">

    /**
     *
     * @param sprintId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/sprint-detail", method = RequestMethod.GET)
    public ResponseEntity<?> getSprintDetail(@RequestParam(value = "sprintId") int sprintId) throws Exception {
        return sprintService.getSprintDetail(sprintId);
    }
    //</editor-fold>

    //<editor-fold desc="Review Sprint By Lecturer">
    @CrossOrigin
    @RequestMapping(value = "/lecturer-review-sprint", method = RequestMethod.POST)
    public ResponseEntity<?> reviewSprintByLecturer(@RequestParam(value = "sprintId") int sprintId,
                                                    @RequestParam float score) throws Exception {
        return sprintService.reviewSprintByLecturer(sprintId,score);
    }
    //</editor-fold>

    //<editor-fold desc="update sprint review, set fileUrl to null">
    /**
     *
     * @param sprintId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/review-sprint", method = RequestMethod.PUT)
    public ResponseEntity<?> updateSprintReviewBySprintId(@RequestParam int sprintId) throws Exception {
        return sprintService.updateSprintReviewBySprintId(sprintId);
    }
    //</editor-fold>

    /**
     * -------------------------------TASK--------------------------------
     */

    //<editor-fold desc="Create New Task">
    /**
     *
     * @param newTask
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public ResponseEntity<?> createNewTask(@RequestBody TaskCreatingRequestDto newTask) throws Exception {
        return taskService.createNewTask(newTask);
    }
    //</editor-fold>

    //<editor-fold desc="Update Task">

    /**
     *
     * @param taskId
     * @param updateTask
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/task", method = RequestMethod.PUT)
    public ResponseEntity<?> updateTaskByTaskId(@RequestParam int taskId,
                                                    @RequestBody TaskUpdateRequestDto updateTask) throws Exception {
        return taskService.updateTask(taskId, updateTask);
    }
    //</editor-fold>

    //<editor-fold desc="Delete Task">

    /**
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/task", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTaskByTaskId(@RequestParam int taskId) throws Exception {
        return taskService.deleteTaskByTaskId(taskId);
    }
    //</editor-fold>

    //<editor-fold desc="View Task detail by ID">
    /**
     *
     * @param taskId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public ResponseEntity<?> viewTaskDetailByTaskId(@RequestParam int taskId) throws  Exception{
        return taskService.viewTaskDetailByTaskId(taskId);
    }
    //</editor-fold>

    //<editor-fold desc="View Task By Sprint and Status">
    /**
     *
     * @param sprintId
     * @param status
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/task", params = "sprintId", method = RequestMethod.GET)
    public ResponseEntity<?> viewTaskListBySprintAndStatus(@RequestParam(value = "sprintId") int sprintId,
                                                             @RequestParam(value = "status") String status) throws Exception {
        return taskService.viewTaskListBySprintAndStatus(sprintId,status);
    }
    //</editor-fold>
    /**
     * -------------------------------TASK COMMENT--------------------------------
     */
    //<editor-fold desc="Create New Comment">
    /**
     *
     * @param newComment
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/task-comment", method = RequestMethod.POST)
    public ResponseEntity<?> createNewCommentInTask(@RequestBody TaskCommentRequestDto newComment) throws Exception {
        return taskCommentService.createNewCommentInTask(newComment);
    }
    //</editor-fold>

    //<editor-fold desc="Delete comment">
    /**
     *
     * @param commentId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/task-comment", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCommentByCommentId(@RequestParam int commentId) throws Exception {
        return taskCommentService.deleteCommentByCommentId(commentId);
    }
    //</editor-fold>

    //<editor-fold desc="Update Comment">

    /**
     *
     * @param commentId
     * @param commentUpdateRequestDto
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/task-comment", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCommentByCommentId(@RequestParam int commentId,
                                                @RequestBody CommentUpdateRequestDto commentUpdateRequestDto) throws Exception {
        return taskCommentService.updateComment(commentId, commentUpdateRequestDto);
    }
    //</editor-fold>

    //<editor-fold desc="View List Comment">

    /**
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/task-comment", method = RequestMethod.GET)
    public ResponseEntity<?> viewCommentListInTask(@RequestParam(value = "taskId") int taskId) throws Exception {
        return taskCommentService.viewCommentListInTask(taskId);
    }
    //</editor-fold>

    /**
     * -------------------------------History--------------------------------
     */

    //<editor-fold desc="View History List">

    /**
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ResponseEntity<?> viewHistoryListInTask(@RequestParam(value = "taskId") int taskId) throws Exception {
        return taskHistoryService.viewHistoryListInTask(taskId);
    }
    //</editor-fold>

    /**
     * -------------------------------Permission--------------------------------
     */

    //<editor-fold desc="Get Permission">

    /**
     *
     * @param roleId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    public ResponseEntity<?> getPermission(@RequestParam(value = "roleId") int roleId) throws Exception {
        return permissionOfRoleService.getPermission(roleId);
    }
    //</editor-fold>

    /**
     * -------------------------------FireBase--------------------------------
     */

    //<editor-fold desc="Upload Image via Firebase">

    /**
     * @param file
     * @param taskId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/upload-file", method = RequestMethod.POST)

    public ResponseEntity<?> upload(@RequestParam(value = "file") List<MultipartFile> file,
                                    @RequestParam(value = "taskId") int taskId) throws Exception {
        return fireBaseService.uploadToThisMachine(file, taskId);
    }
    //</editor-fold>

    /**
     * -------------------------------MEETING--------------------------------
     */

    //<editor-fold desc="Create New Meeting In Project">
    /**
     *
     * @param reqBody
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/meetings", method = RequestMethod.POST)
    public ResponseEntity<?> createNewMeetingsInProject(@RequestBody HashMap<String, String> reqBody) throws Exception {
        return meetingService.createNewMeetingsInProject(reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="Update Meeting Link By Leader">
    /**
     *
     * @param meetingId
     * @param reqBody
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/meetings", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeetingLinkByLeader(@RequestParam int meetingId,
                                                      @RequestBody HashMap<String,String> reqBody) throws Exception {
        return meetingService.updateMeetingLinkByLeader(meetingId, reqBody);
    }
    //</editor-fold>

    //<editor-fold desc="Get Meeting In Project">
    /**
     * @param projectId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/meetings", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingsInProject(@RequestParam int projectId) throws Exception {
        return meetingService.getMeetingsInProject(projectId);
    }
    //</editor-fold>

    //<editor-fold desc="Delete All Meeting">

    /**
     *
     * @param projectId
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping(value = "/meetings", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAllMeetingInProject(@RequestParam int projectId) throws Exception {
        return meetingService.deleteAllMeetingsInProject(projectId);
    }
    //</editor-fold>



}
