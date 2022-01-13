package mgpt.service;

import mgpt.dao.Account;
import mgpt.model.LoginRequestDto;
import mgpt.model.LoginResponseDto;
import mgpt.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public ResponseEntity<?> checkLogin(LoginRequestDto loginRequestDto) throws Exception {
           try{

            Account account = accountRepository.findAccountByUsername(loginRequestDto.getUsername());

            // Check whether account is available or not
            if (account.equals("")) {
                throw new Exception("User is not available");
            }
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setName(account.getName());
            loginResponseDto.setUsername(account.getUsername());
            loginResponseDto.setRoleName("Member");
            return ResponseEntity.ok(loginResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
