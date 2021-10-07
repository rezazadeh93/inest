package ink.nest.inest.controller.v1;

import ink.nest.inest.api.v1.model.OtherLinkDTO;
import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.exception.ResourceNotFoundException;
import ink.nest.inest.service.OtherLinkCrudService;
import ink.nest.inest.utility.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(InestApiConstant.API_V1_PATH)
public class OtherController {
    private final OtherLinkCrudService otherLinkCrudService;
    private final Messages messages;

    public OtherController(OtherLinkCrudService otherLinkCrudService, Messages messages) {
        this.otherLinkCrudService = otherLinkCrudService;
        this.messages = messages;
    }


    @GetMapping("/link/others")
    @ResponseStatus(HttpStatus.OK)
    Set<OtherLinkDTO> getAllOtherLinks() {
        return otherLinkCrudService.getAll();
    }

    @GetMapping("/link/other")
    @ResponseStatus(HttpStatus.OK)
    OtherLinkDTO getOtherLink(@RequestParam("name") String name) {
        return otherLinkCrudService.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                                messages.getExceptionMessage("message.notFound",
                                        List.of(name))
                        )
                );
    }

    @PostMapping("/link/other")
    @ResponseStatus(HttpStatus.CREATED)
    OtherLinkDTO createOtherLink(@Valid @RequestBody OtherLinkDTO otherLink) {
        return otherLinkCrudService.saveOtherLink(otherLink)
                .orElseThrow(() -> new ResourceNotFoundException(
                                messages.getExceptionMessage("message.notFound",
                                        List.of(otherLink.getName()))
                        )
                );
    }

    @PutMapping("/link/other")
    @ResponseStatus(HttpStatus.OK)
    OtherLinkDTO updateOtherLink(@Valid @RequestBody OtherLinkDTO otherLink) {
        return otherLinkCrudService.updateOtherLink(otherLink)
                .orElseThrow(() -> new ResourceNotFoundException(
                                messages.getExceptionMessage("message.notFound",
                                        List.of(otherLink.getName()))
                        )
                );
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/link/other")

    public void deleteLink(@RequestParam("name") String name) {
        otherLinkCrudService.deleteByName(name);
    }
}
