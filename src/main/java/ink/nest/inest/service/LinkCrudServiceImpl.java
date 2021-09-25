package ink.nest.inest.service;

import ink.nest.inest.domain.Account;
import ink.nest.inest.domain.Link;
import ink.nest.inest.exception.ExceptionMessages;
import ink.nest.inest.repository.LinkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public LinkCrudServiceImpl(LinkRepository linkRepository,
                               AccountCrudService accountCrudService) {
        this.linkRepository = linkRepository;
        this.accountCrudService = accountCrudService;
    }

    @Override
    public Set<Link> getAllByUsername(@Nullable String username) {

        if (Objects.isNull(username)) {
            return StreamSupport
                    .stream(linkRepository.findAll().spliterator(), false)
                    .collect(Collectors.toSet());
        }

        Account account = accountCrudService.findAccountByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                ExceptionMessages.getNotFoundException(username)
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
                    .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    ExceptionMessages.getNotFoundException(
                                            linkToSave.getId().toString()
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
