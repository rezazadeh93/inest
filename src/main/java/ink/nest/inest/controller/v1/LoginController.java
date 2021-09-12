package ink.nest.inest.controller.v1;

import ink.nest.inest.api.v1.mapper.AccountMapper;
import ink.nest.inest.api.v1.model.AccountDTO;
import ink.nest.inest.api.v1.model.TokenDTO;
import ink.nest.inest.api.v1.request.AuthRequest;
import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.domain.Account;
import ink.nest.inest.security.JwtTokenUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(InestApiConstant.API_AUTH_PATH)
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final AccountMapper accountMapper;
    private final JwtTokenUtil jwtTokenUtil;

    public LoginController(AuthenticationManager authenticationManager, AccountMapper accountMapper, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.accountMapper = accountMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AccountDTO> login(@RequestBody @Valid AuthRequest authRequest) {
        var authToken = new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(),
                authRequest.getPassword()
        );

        Authentication authenticate = authenticationManager.authenticate(authToken);

        Account user = (Account) authenticate.getPrincipal();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        jwtTokenUtil.generateAccessToken(user)
                )
                .body(accountMapper.accountToAccountDTO(user));
    }

    @GetMapping("user")
    public String findOne(@RequestParam("id") Long id) {
        return "Received " + id + " Users :D";
    }

    @GetMapping("users")
    @ResponseStatus(HttpStatus.OK)
    public String allUsers() {
        return "Received All Users :)))";
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("user")
    public TokenDTO create(@RequestBody AccountDTO accountDTO) {
        return null;
    }
}
