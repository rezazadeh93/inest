package ink.nest.inest.controller.v1;

import ink.nest.inest.api.v1.model.SocialDTO;
import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.exception.ExceptionMessages;
import ink.nest.inest.service.SocialCrudService;
import ink.nest.inest.service.SocialCrudServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.SecondaryTable;
import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(InestApiConstant.API_V1_PATH)
public class SocialController {
    private final SocialCrudService socialCrudService;

    public SocialController(SocialCrudService socialCrudService) {
        this.socialCrudService = socialCrudService;
    }

    @GetMapping("socials")
    @ResponseStatus(HttpStatus.OK)
    public Set<SocialDTO> getAll(@RequestParam("linkID") Long linkID) {
        return socialCrudService.getAllByLinkID(linkID);
    }

    @GetMapping("social")
    @ResponseStatus(HttpStatus.OK)
    public SocialDTO getSocialByLinkIdAndID(@RequestParam("id") Long id, @RequestParam("linkID") Long linkID) {
        return socialCrudService.findByIdAndLinkID(id, linkID)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                ExceptionMessages.getNotFoundException(id.toString())
                        )
                );
    }

    @PostMapping("social")
    @ResponseStatus(HttpStatus.CREATED)
    public SocialDTO connectNewSocial(@Valid @RequestBody SocialDTO socialDTO) {
        return socialCrudService.saveBySocialDTO(socialDTO)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                ExceptionMessages.getInternalSeverException(socialDTO.getName())
                        )
                );
    }

    @PutMapping("social")
    @ResponseStatus(HttpStatus.OK)
    public SocialDTO updateSocial(@Valid @RequestBody SocialDTO socialDTO) {
        return connectNewSocial(socialDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("social")
    public void disconnectSocial(@RequestParam("id") Long id, @RequestParam("linkID") Long linkID) {
        socialCrudService.softDeleteByID(id, linkID);
    }
}
