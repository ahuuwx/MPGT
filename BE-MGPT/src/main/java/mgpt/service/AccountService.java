package mgpt.service;

import mgpt.dao.Account;
import mgpt.model.LoginRequestDto;
import mgpt.model.LoginResponseDto;
import mgpt.repository.AccountRepository;
import mgpt.repository.RoleRepository;
import mgpt.util.Constant;
import mgpt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    //<editor-fold desc="check login">
    public ResponseEntity<?> checkLogin(LoginRequestDto loginRequestDto) throws Exception {
        try {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
                );
            } catch (BadCredentialsException e) {
                throw new Exception("INVALID USERNAME OR PASSWORD", e);
            }

            final UserDetails userDetails = loadUserByUsername(loginRequestDto.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);
            Account account = accountRepository.findAccountByUsername(loginRequestDto.getUsername());

            // Check whether account is available or not
            if (account == null || !account.getPassword().matches(loginRequestDto.getPassword())) {
                throw new Exception("Account is not available");
            }


            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setName(account.getName());
            loginResponseDto.setUsername(account.getUsername());
            loginResponseDto.setRoleName(account.getRole().getRoleName());
            loginResponseDto.setJwt(jwt);
            return ResponseEntity.ok(loginResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //</editor-fold>
    //<editor-fold desc="Load user by Username">
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account acc = accountRepository.findAccountByUsername(username);
        return new User(acc.getUsername(), acc.getPassword(), new ArrayList<>());
    }

    //</editor-fold>
    //<editor-fold desc="get profile by username">
    public ResponseEntity<?> getProfileByUsername(String username) {

        Account account = accountRepository.findAccountByUsername(username);
        HashMap<String, Object> mapObj = new LinkedHashMap<>();
        mapObj.put("username", account.getUsername());
        mapObj.put("password", account.getPassword());
        mapObj.put("name", account.getName());
        mapObj.put("email", account.getEmail());
        return ResponseEntity.ok(mapObj);
    }
    //</editor-fold>

    //<editor-fold desc="get member list in project">
    public ResponseEntity<?> getMemBerInProject(int projectId) {

        List<Account> accountList = accountRepository.findDistinctByRole_RoleIdAndProjectOfUser_Project_ProjectId(Constant.MEMBER_ROLE_ID, projectId);
        Account account = accountRepository.findAccountByRole_RoleIdAndProjectOfUser_Project_ProjectId(Constant.LEADER_ROLE_ID, projectId);
        accountList.add(account);
        List<String> stringList = new ArrayList<>();
        for (Account account1 : accountList) {
            stringList.add(account1.getName());
        }

        HashMap<String, Object> mapObj = new LinkedHashMap<>();
        mapObj.put("username", stringList);
        return ResponseEntity.ok(mapObj);
    }
    //</editor-fold>
}
