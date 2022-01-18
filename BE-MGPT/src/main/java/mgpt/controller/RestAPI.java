package mgpt.controller;

import mgpt.model.LoginRequestDto;
import mgpt.service.AccountService;
import mgpt.service.ProjectService;
import mgpt.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@EnableScheduling
public class RestAPI {

    @Autowired
    AccountService accountService;
    @Autowired
    ProjectService projectService;
    @Autowired
    SprintService sprintService;

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
    @RequestMapping(value = "/project", method = RequestMethod.GET)
    public ResponseEntity<?> getProjectDetailByProjectId(@RequestParam(value = "projectId") int projectId) throws Exception {
        return projectService.getProjectDetailByProjectId(projectId);
    }
    //</editor-fold>
    /**
     * -------------------------------SPRINT--------------------------------
     */

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
}
