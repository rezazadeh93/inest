package ink.nest.inest.controller.v1;

import ink.nest.inest.api.v1.mapper.LinkMapper;
import ink.nest.inest.api.v1.model.LinkDTO;
import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.domain.Link;
import ink.nest.inest.exception.InternalServerException;
import ink.nest.inest.exception.ResourceNotFoundException;
import ink.nest.inest.security.JwtTokenUtil;
import ink.nest.inest.service.AccountCrudService;
import ink.nest.inest.service.LinkCrudService;
import ink.nest.inest.utility.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(InestApiConstant.API_V1_PATH)
public class LinkController {
    private final AccountCrudService accountCrudService;
    private final LinkMapper linkMapper;
    private final LinkCrudService linkCrudService;
    private final JwtTokenUtil jwtTokenUtil;
    private final Messages messages;

    public LinkController(AccountCrudService accountCrudService,
                          LinkMapper linkMapper,
                          LinkCrudService linkCrudService,
                          JwtTokenUtil jwtTokenUtil,
                          Messages messages) {
        this.accountCrudService = accountCrudService;
        this.linkMapper = linkMapper;
        this.linkCrudService = linkCrudService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.messages = messages;
    }

    @GetMapping("links")
    public Set<LinkDTO> getAll(WebRequest request) {
        //get token from header
        String token = Objects.requireNonNull(request.getHeader(InestApiConstant.HEADER_AUTHORIZATION))
                .replace(InestApiConstant.HEADER_BEARER, "");

        //get username from token
        return linkCrudService
                .getAllByUsername(jwtTokenUtil.getUsername(token))
                .stream()
                .map(linkMapper::linkToLinkDTO)
                .collect(Collectors.toSet());
    }

    @GetMapping("link")
    @ResponseStatus(HttpStatus.OK)
    public LinkDTO getLink(@RequestParam("id") Long id) {
        return linkMapper.linkToLinkDTO
                (
                        linkCrudService.findLinkByID(id)
                                .orElseThrow(
                                        () -> new ResourceNotFoundException(
                                                messages.getExceptionMessage("message.notFound",
                                                        List.of(id))
                                        )
                                )
                );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @PostMapping("link")
    public LinkDTO createLink(@Valid @RequestBody LinkDTO linkRequest, WebRequest request) {
        //get token from header
        String token = Objects.requireNonNull(request.getHeader(InestApiConstant.HEADER_AUTHORIZATION))
                .split(" ")[1];

        String username = jwtTokenUtil.getUsername(token);


        linkRequest.setAccountID(null);
        linkRequest.setSocials(null);
        linkRequest.setTemplate(null);
        Link linkFound = linkMapper.dtoLinkToLink(linkRequest);
        linkFound.setAccount(
                accountCrudService.findAccountByEmail(username)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        messages.getExceptionMessage("message.notFound",
                                                List.of(username))
                                )
                        )
        );

        return linkMapper.linkToLinkDTO(
                linkCrudService.saveLinkByAccount(linkFound)
                        .orElseThrow(
                                () -> new InternalServerException(
                                        messages.getExceptionMessage("message.internalServerError",
                                                List.of(username))
                                )
                        )
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("link")
    public LinkDTO updateLink(@Valid @RequestBody LinkDTO linkRequest, WebRequest request) {
        return createLink(linkRequest, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("link")
    public void deleteLink(@RequestParam("id") Long id) {
        linkCrudService.softDeleteByID(id);
    }
}
