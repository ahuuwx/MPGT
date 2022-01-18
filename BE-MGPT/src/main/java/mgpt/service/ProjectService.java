package mgpt.service;

import mgpt.dao.Account;
import mgpt.dao.Project;
import mgpt.dao.ProjectOfUser;
import mgpt.model.ProjectListResponseDto;
import mgpt.repository.AccountRepository;
import mgpt.repository.ProjectOfUserRepository;
import mgpt.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ProjectOfUserRepository projectOfUserRepository;
    public ResponseEntity<?> getProjectsByUsername(String username){
        try {
            Account account=accountRepository.findAccountByUsername(username) ;

            List<ProjectOfUser> projectOfUser=projectOfUserRepository.findProjectOfUserByUsername(account);
            List<Project> projectList=projectRepository.findAllByProjectOfUserListIsIn(projectOfUser);
            List<ProjectListResponseDto> projectListResponseDtos=projectList.stream().map(project -> project.convertToProjectDto()).collect(Collectors.toList());
            ProjectOfUser projectOfUser1=projectOfUser.get(0);

//            for (ProjectListResponseDto projectListResponseDto: projectListResponseDtos) {
//                Account accountLeader=
//                        accountRepository.findAccountByRoleOfUser_RoleId_RoleIdAndProjectOfUser_ProjectOfUserId(5, projectOfUser1.getProjectOfUserId());
//                projectListResponseDto.setLeaderName(accountLeader.getName());
//            }
            return ResponseEntity.ok(projectListResponseDtos);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    }
}
