package ink.nest.inest.service;

import ink.nest.inest.api.v1.model.LinkDTO;
import ink.nest.inest.api.v1.model.SocialDTO;
import ink.nest.inest.exception.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class SocialCrudServiceImpl implements SocialCrudService {
    private final LinkCrudService linkCrudService;

    public SocialCrudServiceImpl(LinkCrudService linkCrudService) {
        this.linkCrudService = linkCrudService;
    }

    @Override
    public Set<SocialDTO> getAllByLinkID(@NonNull final Long linkID) {
        log.debug("logging service: @getAllByLinkID => id : " + linkID);

        // find link related to social by link id
        return linkCrudService.findByID(linkID)
                .map(LinkDTO::getSocials)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        ExceptionMessages.getNotFoundException(linkID.toString())
                ));
    }

    @Override
    public Optional<SocialDTO> findByIdAndLinkID(@NonNull Long id, @NonNull Long linkID) {
        log.debug("logging service: @findByIdAndLinkID => id : " + linkID);

        LinkDTO linkFound = getLinkByID(linkID);

        return getSocialByIdAndLink(id, linkFound);
    }

    @Override
    public Optional<SocialDTO> saveBySocialDTO(@NonNull final SocialDTO socialDTO, @NonNull Long linkID) {
        log.debug("logging service: @saveBySocialDTO => id : " + linkID);
        LinkDTO linkFound = getLinkByID(linkID);

        // if social doesn't exist already and it's POST Method
        if (Objects.isNull(socialDTO.getId())) {
            linkFound.getSocials()
                    .add(socialDTO);
        } else {
            // if social exist and it's PUT METHOD
            linkFound.getSocials()
                    .forEach(social -> {
                        if (social.getId().equals(socialDTO.getId())) {
                            social.setName(socialDTO.getName());
                            social.setLabel(socialDTO.getLabel());
                            social.setUrl(socialDTO.getUrl());
                        }
                    });
        }

        return linkCrudService.saveLinkDtoByAccount(linkFound, linkFound.getAccountID())
                .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                ExceptionMessages.getInternalSeverException(linkID.toString())
                        )
                ).getSocials()
                .stream()
                .filter(social -> social.getId().equals(socialDTO.getId()))
                .findFirst();
    }

    @Override
    public void softDeleteByID(@NonNull Long id, @NonNull Long linkID) {
        log.debug("logging service: @softDeleteByIdAccount => id : " + id);

        LinkDTO linkFound = getLinkByID(linkID);

        if (!linkFound.getSocials().removeIf(socialDTO -> socialDTO.getId().equals(id)))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ExceptionMessages.getInternalSeverException(id.toString())
            );
    }

    private LinkDTO getLinkByID(@NonNull Long linkID) {
        return linkCrudService.findByID(linkID)
                .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                ExceptionMessages.getNotFoundException(linkID.toString())
                        )
                );
    }

    private Optional<SocialDTO> getSocialByIdAndLink(@NonNull Long id, @NonNull final LinkDTO linkFound) {
        return linkFound.getSocials()
                .stream()
                .filter(socialDTO -> socialDTO.getId().equals(id))
                .findFirst();
    }
}
