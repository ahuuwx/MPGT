package mgpt.controller;

import mgpt.model.LoginRequestDto;
import mgpt.model.LoginResponseDto;
import mgpt.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.HashMap;

@RestController
@EnableScheduling
public class RestAPI {

    @Autowired
    AccountService accountService;
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

}
