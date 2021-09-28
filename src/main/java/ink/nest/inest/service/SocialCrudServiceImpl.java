package ink.nest.inest.service;

import ink.nest.inest.domain.Link;
import ink.nest.inest.domain.Social;
import ink.nest.inest.exception.InternalServerException;
import ink.nest.inest.exception.ResourceNotFoundException;
import ink.nest.inest.utility.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class SocialCrudServiceImpl implements SocialCrudService {
    private final LinkCrudService linkCrudService;
    private final Messages messages;

    public SocialCrudServiceImpl(LinkCrudService linkCrudService, Messages messages) {
        this.linkCrudService = linkCrudService;
        this.messages = messages;
    }

    @Override
    public Set<Social> getAllByLinkID(@NonNull final Long linkID) {
        log.debug("logging service: @getAllByLinkID => id : " + linkID);

        // find link related to social by link id
        return linkCrudService.findLinkByID(linkID)
                .orElseThrow(() -> new ResourceNotFoundException(
                                messages.getExceptionMessage(
                                        "message.notFound",
                                        Collections.singletonList("ID=" + linkID)
                                )
                        )
                )
                .getSocials();
    }

    @Override
    public Optional<Social> findByIdAndLinkID(@NonNull final Long id, @NonNull Long linkID) {
        log.debug("logging service: @findByIdAndLinkID => id : " + linkID);

        return getLinkByID(linkID)
                .getSocials()
                .stream()
                .filter(socialDTO -> socialDTO.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Social> findByNameAndLinkID(@NonNull final String name, @NonNull Long linkID) {
        log.debug("logging service: @findByNameAndLinkID => id : " + name);

        return getLinkByID(linkID)
                .getSocials()
                .stream()
                .filter(social -> social.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public Optional<Social> updateBySocial(@NonNull final Social socialToSave) {
        log.debug("logging service: @updateBySocialDTO => id : " + socialToSave.getName());

        Link linkFound = socialToSave.getLink();

        // if social ID is null find by name in PUT Method
        if (!Objects.isNull(socialToSave.getId())) {

            // if social exist and it's PUT METHOD
            linkFound.getSocials()
                    .forEach(social -> {
                        if (social.getId().equals(socialToSave.getId())) {
                            social.setLabel(socialToSave.getLabel());
                            social.setUrl(socialToSave.getUrl());
                        }
                    });
        } else {

            // if social exist and it's PUT METHOD
            linkFound.getSocials()
                    .forEach(social -> {
                        if (social.getName().equals(socialToSave.getName())) {
                            social.setName(socialToSave.getName());
                            social.setLabel(socialToSave.getLabel());
                            social.setUrl(socialToSave.getUrl());
                        }
                    });
        }

        return linkCrudService.saveLinkByAccount(linkFound)
                .orElseThrow(() -> new InternalServerException(
                                messages.getExceptionMessage("message.internalServerError", Collections.singletonList(linkFound.getId()))
                        )
                ).getSocials()
                .stream()
                .filter(social -> social.getId().equals(socialToSave.getId()))
                .findFirst();
    }

    @Override
    public Optional<Social> saveBySocial(@NonNull final Social socialToSave) {
        log.debug("logging service: @saveBySocialDTO => id : " + socialToSave.getName());

        Link linkFound = socialToSave.getLink();

        // check if this name already created in table or not
        boolean nameExist = linkFound.getSocials()
                .stream()
                .anyMatch(
                        social -> social.getName().equalsIgnoreCase(socialToSave.getName())
                );

        // throw an exception if name exist already for POST method
        if (Objects.isNull(socialToSave.getId()) && nameExist)
            throw new ResourceNotFoundException(
                    messages.getExceptionMessage("message.thisEmailExist", Collections.singletonList(socialToSave.getName()))
            );

        // if social doesn't exist already and it's POST Method
        if (Objects.isNull(socialToSave.getId())) {
            linkFound.getSocials()
                    .add(socialToSave);
        }

        return linkCrudService.saveLinkByAccount(linkFound)
                .orElseThrow(() -> new InternalServerException(
                                messages.getExceptionMessage("message.internalServerError", Collections.singletonList(linkFound.getId()))
                        )
                ).getSocials()
                .stream()
                .filter(social -> social.getName().equals(socialToSave.getName()))
                .findFirst();
    }

    @Override
    public void softDeleteByID(@NonNull final Long id, @NonNull Long linkID) {
        log.debug("logging service: @softDeleteByIdAccount => id : " + id);

        final Link linkFound = getLinkByID(linkID);

        if (!linkFound.getSocials().removeIf(socialDTO -> socialDTO.getId().equals(id)))
            throw new ResourceNotFoundException(
                    messages.getExceptionMessage("message.notFound", Collections.singletonList(id))
            );

        linkCrudService.saveLinkByAccount(linkFound)
                .orElseThrow(() -> new InternalServerException(
                                messages.getExceptionMessage("message.internalServerError", Collections.singletonList(id))
                        )
                );
    }

    private Link getLinkByID(@NonNull Long linkID) {
        return linkCrudService.findLinkByID(linkID)
                .orElseThrow(() -> new ResourceNotFoundException(
                                messages.getExceptionMessage("message.notFound", Collections.singletonList(linkID))
                        )
                );
    }
}
