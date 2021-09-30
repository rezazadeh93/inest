package ink.nest.inest.controller.v1;

import ink.nest.inest.api.v1.mapper.SocialMapper;
import ink.nest.inest.api.v1.model.SocialDTO;
import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.domain.Link;
import ink.nest.inest.domain.Social;
import ink.nest.inest.exception.InternalServerException;
import ink.nest.inest.exception.ResourceNotFoundException;
import ink.nest.inest.service.LinkCrudService;
import ink.nest.inest.service.SocialCrudService;
import ink.nest.inest.service.SocialNameCrudService;
import ink.nest.inest.utility.Messages;
import ink.nest.inest.validation.PostMethod;
import ink.nest.inest.validation.PutMethod;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(InestApiConstant.API_V1_PATH)
public class SocialController {
    private final LinkCrudService linkCrudService;
    private final SocialCrudService socialCrudService;
    private final SocialNameCrudService socialNameCrudService;
    private final SocialMapper socialMapper;
    private final Messages messages;

    public SocialController(LinkCrudService linkCrudService,
                            SocialCrudService socialCrudService,
                            SocialNameCrudService socialNameCrudService, SocialMapper socialMapper, Messages messages) {
        this.linkCrudService = linkCrudService;
        this.socialCrudService = socialCrudService;
        this.socialNameCrudService = socialNameCrudService;
        this.socialMapper = socialMapper;
        this.messages = messages;
    }

    @GetMapping("social/names")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> getAllSocialNames() {
        return socialNameCrudService.getAll();
    }

    @GetMapping("socials")
    @ResponseStatus(HttpStatus.OK)
    public Set<SocialDTO> getAll(@RequestParam("linkID") Long linkID) {
        return socialCrudService
                .getAllByLinkID(linkID)
                .stream()
                .map(socialMapper::socialToSocialDTO)
                .collect(Collectors.toSet());
    }

    @GetMapping("social")
    @ResponseStatus(HttpStatus.OK)
    public SocialDTO getSocialByLinkIdAndID(@RequestParam("id") Long id, @RequestParam("linkID") Long linkID) {
        return socialMapper.socialToSocialDTO(
                socialCrudService.findByIdAndLinkID(id, linkID)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        messages.getExceptionMessage("message.notFound",
                                                List.of(id))
                                )
                        )
        );
    }

    @PostMapping("social")
    @ResponseStatus(HttpStatus.CREATED)
    public SocialDTO connectNewSocial(@Validated(PostMethod.class) @RequestBody SocialDTO socialDTO) {
        socialDTO.setId(null);
        socialDTO.setName(socialDTO.getName().toUpperCase());

        Social social = socialMapper.dotSocialTOSocial(socialDTO);
        social.setLink(
                linkCrudService.findLinkByID(socialDTO.getLinkID())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                        messages.getExceptionMessage("message.notFound",
                                                List.of(socialDTO.getLinkID()))
                                )
                        )
        );

        return socialMapper.socialToSocialDTO(
                socialCrudService.saveBySocial(social)
                        .orElseThrow(
                                () -> new InternalServerException(
                                        messages.getExceptionMessage("message.internalServerError",
                                                List.of(socialDTO.getName()))
                                )
                        )
        );
    }

    @PutMapping("social")
    @ResponseStatus(HttpStatus.OK)
    public SocialDTO updateSocial(@Validated(PutMethod.class) @RequestBody SocialDTO socialDTO) {
        Link linkFound = linkCrudService.findLinkByID(socialDTO.getLinkID())
                .orElseThrow(() -> new ResourceNotFoundException(
                                messages.getExceptionMessage("message.notFound",
                                        List.of(socialDTO.getLinkID()))
                        )
                );

        // check if this name already created in table or not
        // throw an exception if name exist already for POST method
        if (linkFound.getSocials()
                .stream()
                .noneMatch(
                        social -> social.getId().equals(socialDTO.getId())
                )
        )
            throw new ResourceNotFoundException(
                    messages.getExceptionMessage("message.notFound",
                            List.of(socialDTO.getLinkID()))
            );

        Social social = socialMapper.dotSocialTOSocial(socialDTO);
        social.setLink(linkFound);

        return socialMapper.socialToSocialDTO(
                socialCrudService.updateBySocial(social)
                        .orElseThrow(
                                () -> new InternalServerException(
                                        messages.getExceptionMessage("message.internalServerError",
                                                List.of(socialDTO.getName()))
                                )
                        )
        );
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("social")
    public void disconnectSocial(@RequestParam("id") Long id, @RequestParam("linkID") Long linkID) {
        socialCrudService.softDeleteByID(id, linkID);
    }
}
