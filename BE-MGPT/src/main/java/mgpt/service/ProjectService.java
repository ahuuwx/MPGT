package mgpt.service;

import mgpt.dao.Account;
import mgpt.dao.Project;
import mgpt.dao.ProjectOfUser;
import mgpt.model.ProjectDetailResponseDto;
import mgpt.model.ProjectListResponseDto;
import mgpt.model.ProjectListSearchByDateDto;
import mgpt.repository.AccountRepository;
import mgpt.repository.ProjectOfUserRepository;
import mgpt.repository.ProjectRepository;
import mgpt.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    //<editor-fold desc="Get Projects By Username">
    public ResponseEntity<?> getProjectsByUsername(String username) {
        try {
            Account account = accountRepository.findAccountByUsername(username);
            if (account != null) {
                /**
                 * ROLE: MEMBER
                 */
                if (account.getRole().getRoleName().matches(Constant.MEMBER_ROLE_NAME)) {
                    ProjectOfUser projectOfUser = projectOfUserRepository.findProjectOfUserByUsername(account);
                    Project project = projectRepository.findProjectByProjectOfUser_ProjectId(projectOfUser.getProject().getProjectId());
                    ProjectListResponseDto projectListResponseDto = project.convertToProjectDto();
                    Account accountLeader = accountRepository.findAccountByRole_RoleIdAndProjectOfUser_Project_ProjectId(Constant.LEADER_ROLE_ID, projectOfUser.getProject().getProjectId());
                    projectListResponseDto.setLeaderName(accountLeader.getName());
                    List<String> lecturerName = new ArrayList<>();
                    List<Account> accountList = accountRepository.findDistinctByRole_RoleIdAndProjectOfUser_Project_ProjectId(Constant.LECTURER_ROLE_ID, projectOfUser.getProject().getProjectId());
                    for (Account accountLecturer : accountList) {
                        lecturerName.add(accountLecturer.getName());
                    }
                    projectListResponseDto.setSemester(project.getSemester());
                    projectListResponseDto.setLecturerName(lecturerName);
                    return ResponseEntity.ok(projectListResponseDto);
                }
                /**
                 * ROLE: Leader
                 */
                if (account.getRole().getRoleName().matches(Constant.LEADER_ROLE_NAME)) {
                    ProjectOfUser projectOfUser = projectOfUserRepository.findProjectOfUserByUsername(account);
                    Project project = projectRepository.findProjectByProjectOfUser_ProjectId(projectOfUser.getProject().getProjectId());
                    ProjectListResponseDto projectListResponseDto = project.convertToProjectDto();
                    projectListResponseDto.setLeaderName(account.getName());
                    List<String> lecturerName = new ArrayList<>();
                    List<Account> accountList = accountRepository.findDistinctByRole_RoleIdAndProjectOfUser_Project_ProjectId(Constant.LECTURER_ROLE_ID, projectOfUser.getProject().getProjectId());
                    for (Account accountLecturer : accountList) {
                        lecturerName.add(accountLecturer.getName());
                    }
                    projectListResponseDto.setSemester(project.getSemester());
                    projectListResponseDto.setLecturerName(lecturerName);
                    return ResponseEntity.ok(projectListResponseDto);
                }
                /**
                 * ROLE: Lecturer
                 */
                if (account.getRole().getRoleName().matches(Constant.LECTURER_ROLE_NAME)) {
                    List<ProjectOfUser> projectOfUserList = projectOfUserRepository.findProjectOfUserByUsername_Username(username);
                    List<Project> projectList = projectRepository.findProjectsByProjectOfUserListIsIn(projectOfUserList);
                    List<ProjectListResponseDto> projectListResponseDto = projectList.stream().map(project -> project.convertToProjectDto()).collect(Collectors.toList());
                    for (ProjectListResponseDto projectListResponseDto1 : projectListResponseDto) {
                        Account accountLeader = accountRepository.findAccountByRole_RoleIdAndProjectOfUser_Project_ProjectId(Constant.LEADER_ROLE_ID, projectListResponseDto1.getProjectId());
                        projectListResponseDto1.setLeaderName(accountLeader.getName());
                        List<String> lecturerName = new ArrayList<>();
                        List<Account> accountList = accountRepository.findDistinctByRole_RoleIdAndProjectOfUser_Project_ProjectId(Constant.LECTURER_ROLE_ID, projectListResponseDto1.getProjectId());
                        for (Account accountLecturer : accountList) {
                            lecturerName.add(accountLecturer.getName());
                        }
                        
                        projectListResponseDto1.setLecturerName(lecturerName);
                    }

                    return ResponseEntity.ok(projectListResponseDto);
                }
            } else {
                throw new Exception(Constant.INVALID_USERNAME);

            }
            return ResponseEntity.ok(Boolean.FALSE);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Get Project Detail By ProjectID">
    public ResponseEntity<?> getProjectDetailByProjectId(int projectId) {
        try {

            Project project = projectRepository.findByProjectId(projectId);
            ProjectDetailResponseDto projectDetailResponseDto = project.convertToProjectDetailDto();
            //query for account leader and lecturer
            Account accountLeader = accountRepository.findAccountByRole_RoleIdAndProjectOfUser_Project_ProjectId(Constant.LEADER_ROLE_ID, projectId);
            List<Account> accountLecturerList = accountRepository.findDistinctByRole_RoleIdAndProjectOfUser_Project_ProjectId(Constant.LECTURER_ROLE_ID, projectId);
            List<String> lecturerNameList = new ArrayList<>();
            for (Account account : accountLecturerList) {
                lecturerNameList.add(account.getName());
            }

            //query for member name list
            List<Account> accountMemberList = accountRepository.findDistinctByRole_RoleIdAndProjectOfUser_Project_ProjectId(Constant.MEMBER_ROLE_ID, projectId);
            List<String> memberNameList = new ArrayList<>();
            for (Account account : accountMemberList) {
                memberNameList.add(account.getName());
            }

            //set data
            projectDetailResponseDto.setLeaderName(accountLeader.getName());
            projectDetailResponseDto.setLecturerName(lecturerNameList);
            projectDetailResponseDto.setMemberList(memberNameList);
            return ResponseEntity.ok(projectDetailResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Get Prject By Date">
    public ResponseEntity<?> getProjectByDate(ProjectListSearchByDateDto dto, String username) {
        try {
            Account account = accountRepository.findAccountByUsername(username);
            if (account.getRole().getRoleName().matches(Constant.LECTURER_ROLE_NAME)) {
                if (dto.getStartDate().after(dto.getEndDate())) {
                    throw new Exception(Constant.INVALID_STARTDATE_ENDDATE);
                }
                List<ProjectOfUser> projectOfUserList = projectOfUserRepository.findProjectOfUserByUsername_UsernameAndProject_StartDateBetween(username, dto.getStartDate(), dto.getEndDate());
                List<Project> projectList = projectRepository.findProjectsByProjectOfUserListIsIn(projectOfUserList);
                List<ProjectListResponseDto> projectListResponseDto = projectList.stream().map(project -> project.convertToProjectDto()).collect(Collectors.toList());
                for (ProjectListResponseDto projectListResponseDto1 : projectListResponseDto) {
                    Account accountLeader = accountRepository.findAccountByRole_RoleIdAndProjectOfUser_Project_ProjectId(Constant.LEADER_ROLE_ID, projectListResponseDto1.getProjectId());
                    projectListResponseDto1.setLeaderName(accountLeader.getName());
                    List<String> lecturerName = new ArrayList<>();
                    List<Account> accountList = accountRepository.findDistinctByRole_RoleIdAndProjectOfUser_Project_ProjectId(Constant.LECTURER_ROLE_ID, projectListResponseDto1.getProjectId());
                    for (Account accountLecturer : accountList) {
                        lecturerName.add(accountLecturer.getName());
                    }
                    projectListResponseDto1.setLecturerName(lecturerName);
                }

                return ResponseEntity.ok(projectListResponseDto);
            }
            throw new Exception(Constant.INVALID_USERNAME);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}


