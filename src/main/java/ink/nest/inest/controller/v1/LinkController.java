package ink.nest.inest.controller.v1;

import ink.nest.inest.api.v1.model.AccountDTO;
import ink.nest.inest.constant.InestApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
