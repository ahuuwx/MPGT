package mgpt.service;

import mgpt.dao.Account;
import mgpt.dao.RoleOfUser;
import mgpt.model.LoginRequestDto;
import mgpt.model.LoginResponseDto;
import mgpt.repository.AccountRepository;
import mgpt.repository.RoleOfUserRepository;
import mgpt.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleOfUserRepository roleOfUserRepository;
    @Autowired
    RoleRepository roleRepository;

    public ResponseEntity<?> checkLogin(LoginRequestDto loginRequestDto) throws Exception {
        try {

            Account account = accountRepository.findAccountByUsername(loginRequestDto.getUsername());

            // Check whether account is available or not
            if (account == null || !account.getPassword().matches(loginRequestDto.getPassword())) {
                throw new Exception("Account is not available");
            }
            RoleOfUser roleOfUser = roleOfUserRepository.findRoleOfUserByAccount_Username(account.getUsername());

            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setName(account.getName());
            loginResponseDto.setUsername(account.getUsername());
            loginResponseDto.setRoleName(roleOfUser.getRoleId().getRoleName());
            return ResponseEntity.ok(loginResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
