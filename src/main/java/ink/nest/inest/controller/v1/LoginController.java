package ink.nest.inest.controller.v1;

import ink.nest.inest.api.v1.model.AccountDTO;
import ink.nest.inest.api.v1.model.TokenDTO;
import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(InestApiConstant.API_V1_PATH)
public class LoginController {
    private final TokenService tokenService;

    public LoginController(TokenService tokenService) {
        this.tokenService = tokenService;
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
        return tokenService.createToken();
    }
}
