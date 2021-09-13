package ink.nest.inest.controller.v1;

import ink.nest.inest.api.v1.model.AccountDTO;
import ink.nest.inest.api.v1.model.LinkDTO;
import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.security.JwtTokenUtil;
import ink.nest.inest.service.LinkCrudService;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@RestController
@RequestMapping(InestApiConstant.API_V1_PATH)
public class LinkController {
    private final LinkCrudService linkCrudService;
    private final JwtTokenUtil jwtTokenUtil;

    public LinkController(LinkCrudService linkCrudService, JwtTokenUtil jwtTokenUtil) {
        this.linkCrudService = linkCrudService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("links")
    public Set<LinkDTO> getAll(HttpServletRequest request) {
        //get token from header
        String token = request.getHeader(InestApiConstant.HEADER_AUTHORIZATION)
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
    public LinkDTO create() {
        //@todo need implement
        return null;
    }
}
