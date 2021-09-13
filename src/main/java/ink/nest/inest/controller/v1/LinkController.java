package ink.nest.inest.controller.v1;

import ink.nest.inest.api.v1.model.LinkDTO;
import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.domain.Account;
import ink.nest.inest.security.JwtTokenUtil;
import ink.nest.inest.service.AccountCrudService;
import ink.nest.inest.service.LinkCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(InestApiConstant.API_V1_PATH)
public class LinkController {
    private final LinkCrudService linkCrudService;
    private final AccountCrudService accountCrudService;
    private final JwtTokenUtil jwtTokenUtil;

    public LinkController(LinkCrudService linkCrudService,
                          AccountCrudService accountCrudService,
                          JwtTokenUtil jwtTokenUtil) {
        this.linkCrudService = linkCrudService;
        this.accountCrudService = accountCrudService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("links")
    public Set<LinkDTO> getAll(WebRequest request) {
        //get token from header
        String token = Objects.requireNonNull(request.getHeader(InestApiConstant.HEADER_AUTHORIZATION))
                .replace(InestApiConstant.HEADER_BEARER, "");

        //get username from token
        return linkCrudService.getAll(jwtTokenUtil.getUsername(token));
    }

    @GetMapping("link")
    @ResponseStatus(HttpStatus.OK)
    public LinkDTO getLink(@RequestParam("id") Long id) {
        return linkCrudService.findByID(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not Found Link+ " + id)
                );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("link")
    public LinkDTO createLink(@RequestParam("name") String name, WebRequest request) {
        //get token from header
        String token = Objects.requireNonNull(request.getHeader(InestApiConstant.HEADER_AUTHORIZATION))
                .split(" ")[1];

        Account activeAccount = accountCrudService.findByEmail(jwtTokenUtil.getUsername(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not Found Account"));

        return linkCrudService.saveLinkDTO(name, activeAccount)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Something went wrong during saving account"
                        )
                );
    }

    public void deleteLink(@RequestParam("id") Long id) {
        linkCrudService.softDeleteByIdAccount(id);
    }
}
