package ink.nest.inest.service;

import ink.nest.inest.domain.Account;
import ink.nest.inest.domain.Link;
import ink.nest.inest.exception.ResourceNotFoundException;
import ink.nest.inest.repository.LinkRepository;
import ink.nest.inest.utility.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class LinkCrudServiceImpl implements LinkCrudService {
    private final LinkRepository linkRepository;
    private final AccountCrudService accountCrudService;
    private final Messages messages;

    public LinkCrudServiceImpl(LinkRepository linkRepository,
                               AccountCrudService accountCrudService, Messages messages) {
        this.linkRepository = linkRepository;
        this.accountCrudService = accountCrudService;
        this.messages = messages;
    }

    @Override
    public Set<Link> getAllByUsername(@Nullable String username) {

        if (Objects.isNull(username)) {
            return StreamSupport
                    .stream(linkRepository.findAll().spliterator(), false)
                    .collect(Collectors.toSet());
        }

        Account account = accountCrudService.findAccountByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                                messages.getExceptionMessage(
                                        "message.notFound",
                                        Collections.singletonList(username)
                                )
                        )
                );

        return Collections.unmodifiableSet(account.getLinks());
    }

    @Override
    public Optional<Link> findLinkByID(@NonNull Long id) {
        return linkRepository.findById(id);
    }

    public Optional<Link> saveLinkByAccount(@NonNull Link linkToSave) {
        log.debug("logging service: @saveLinkDTO => name : " + linkToSave.getName());

        Link savedLink;
        Account account = linkToSave.getAccount();

        // if POST request was send
        if (Objects.isNull(linkToSave.getId())) {
            Link attachLink = new Link();
            attachLink.setName(linkToSave.getName());
            attachLink.setAccount(account);

            savedLink = linkRepository.save(attachLink);

            account.getLinks().add(savedLink);
            accountCrudService.saveAccount(account);
        } else {
            // if PUT request was send
            Link foundLink = linkRepository
                    .findById(linkToSave.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                                    messages.getExceptionMessage(
                                            "message.notFound",
                                            Collections.singletonList(linkToSave.getId())
                                    )
                            )
                    );

            foundLink.setName(linkToSave.getName());
            foundLink.setSocials(linkToSave.getSocials());
            foundLink.setTemplate(linkToSave.getTemplate());
            savedLink = linkRepository.save(foundLink);
        }
        return Optional.of(savedLink);
    }

    @Override
    public void softDeleteByID(@NonNull Long id) {
        log.debug("logging service: @softDeleteByIdAccount => id : " + id);

        linkRepository.deleteById(id);
    }
}
