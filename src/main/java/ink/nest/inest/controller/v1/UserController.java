package ink.nest.inest.controller.v1;

import ink.nest.inest.api.v1.model.AccountDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(UserController.base_url)
public class UserController {
    public static final String base_url = "/api/v1/user";

    @GetMapping(value = "/{id}")
    public String findOne(@PathVariable Long id) {
        return "Received " + id + " Users :D";
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public String allUsers() {
        return "Received All Users :)))";
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public String create(@RequestBody AccountDTO accountDTO) {
        return "Account Created!";
    }
}
