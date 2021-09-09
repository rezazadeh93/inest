package ink.nest.inest.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.nest.inest.api.v1.model.ReqRegisterDTO;
import ink.nest.inest.api.v1.model.TokenDTO;
import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.service.AccountService;
import ink.nest.inest.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.util.Objects;

@RestController
@RequestMapping(InestApiConstant.API_V1_PATH)
public class RegisterController {
    private final TokenService tokenService;
    private final AccountService accountService;

    public RegisterController(TokenService tokenService, AccountService accountService) {
        this.tokenService = tokenService;
        this.accountService = accountService;
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenDTO register(@Valid @RequestBody ReqRegisterDTO account) throws ValidationException, JsonProcessingException {
        // validate Register for confirmation password
        if (!Objects.equals(account.getPassword(), account.getConfirmPassword())) {
            throw new ValidationException("PasswordConfirm doesn't match with password");
        }

        accountService.saveAccountDTO(account);

        return tokenService.createToken();
    }
}