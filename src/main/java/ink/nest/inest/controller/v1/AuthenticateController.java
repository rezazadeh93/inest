package ink.nest.inest.controller.v1;

import ink.nest.inest.api.v1.mapper.AccountMapper;
import ink.nest.inest.api.v1.model.AccountDTO;
import ink.nest.inest.api.v1.request.AuthRequest;
import ink.nest.inest.api.v1.request.GenerateTokenRequest;
import ink.nest.inest.api.v1.request.RegisterRequest;
import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.exception.ExceptionMessages;
import ink.nest.inest.security.JwtTokenUtil;
import ink.nest.inest.service.AccountCrudService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping(InestApiConstant.API_AUTH_PATH)
public class AuthenticateController {
    private final AccountMapper accountMapper;
    private final AccountCrudService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthenticateController(AccountMapper accountMapper, AccountCrudService accountService,
                                  AuthenticationManager authenticationManager,
                                  JwtTokenUtil jwtTokenUtil) {
        this.accountMapper = accountMapper;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.accountService = accountService;
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountDTO> register(@Valid @RequestBody RegisterRequest account) {
        AccountDTO savedAccountDTO = accountService
                .registerNewUserAccount(account)
                .map(accountMapper::accountToAccountDTO)
                .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                ExceptionMessages.getNotFoundException(account.getEmail())
                        )
                );

        GenerateTokenRequest tokenRequest = new GenerateTokenRequest();
        tokenRequest.setUsername(savedAccountDTO.getEmail());

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        jwtTokenUtil.generateAccessToken(tokenRequest)
                )
                .body(savedAccountDTO);

    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AccountDTO> login(@RequestBody @Valid AuthRequest authRequest) {
        var authToken = new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(),
                authRequest.getPassword()
        );

        Authentication authenticate = authenticationManager.authenticate(authToken);

        User UserAuthenticated = (User) authenticate.getPrincipal();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        jwtTokenUtil.generateAccessToken(
                                new GenerateTokenRequest(
                                        UserAuthenticated.getUsername(),
                                        UserAuthenticated.getAuthorities()
                                )
                        )
                )
                .body(
                        new AccountDTO(
                                UserAuthenticated.getUsername()
                        )
                );
    }
}