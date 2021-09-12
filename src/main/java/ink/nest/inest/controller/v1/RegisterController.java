package ink.nest.inest.controller.v1;

import ink.nest.inest.api.v1.mapper.AccountMapper;
import ink.nest.inest.api.v1.model.AccountDTO;
import ink.nest.inest.api.v1.request.RegisterRequest;
import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.domain.Account;
import ink.nest.inest.security.JwtTokenUtil;
import ink.nest.inest.service.AccountCrudService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(InestApiConstant.API_AUTH_PATH)
public class RegisterController {
    private final AccountCrudService accountService;
    private final AccountMapper accountMapper;
    private final JwtTokenUtil jwtTokenUtil;

    public RegisterController(AccountCrudService accountService, AccountMapper accountMapper, JwtTokenUtil jwtTokenUtil) {
        this.accountMapper = accountMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.accountService = accountService;
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountDTO> register(@Valid @RequestBody RegisterRequest account) {
        AccountDTO savedAccountDTO = accountService.registerNewUserAccount(account);
        Account savedAccount = accountMapper.dtoAccountToAccount(savedAccountDTO);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        jwtTokenUtil.generateAccessToken(savedAccount)
                )
                .body(savedAccountDTO);

    }
}