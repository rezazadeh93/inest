package ink.nest.inest.controller.v1;

import ink.nest.inest.api.v1.mapper.AccountMapper;
import ink.nest.inest.api.v1.model.AccountDTO;
import ink.nest.inest.api.v1.model.TokenDTO;
import ink.nest.inest.api.v1.request.AuthRequest;
import ink.nest.inest.api.v1.request.GenerateTokenRequest;
import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.security.JwtTokenUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(InestApiConstant.API_V1_PATH)
public class LinkController {
    @GetMapping("link")
    public String findOne(@RequestParam("id") Long id) {
        return "Received ${id} Link :D";
    }

    @GetMapping("links")
    @ResponseStatus(HttpStatus.OK)
    public String allUsers() {
        return "Received All Links :)))";
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("link")
    public String create(@RequestBody AccountDTO accountDTO) {
        return "Link Created!!!";
    }
}
