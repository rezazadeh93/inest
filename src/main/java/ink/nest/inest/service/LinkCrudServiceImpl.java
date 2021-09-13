package ink.nest.inest.service;

import ink.nest.inest.api.v1.mapper.LinkMapper;
import ink.nest.inest.api.v1.model.LinkDTO;
import ink.nest.inest.api.v1.request.LinkRequest;
import ink.nest.inest.domain.Account;
import ink.nest.inest.domain.Link;
import ink.nest.inest.exception.ExceptionMessages;
import ink.nest.inest.repository.LinkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class LinkCrudServiceImpl implements LinkCrudService {
    private final LinkMapper linkMapper;
    private final LinkRepository linkRepository;
    private final AccountCrudService accountCrudService;

    public LinkCrudServiceImpl(LinkMapper linkMapper,
                               LinkRepository linkRepository,
                               AccountCrudService accountCrudService) {
        this.linkMapper = linkMapper;
        this.linkRepository = linkRepository;
        this.accountCrudService = accountCrudService;
    }

    @Override
    public Set<LinkDTO> getAll(String username) {

        if (Objects.isNull(username)) {
            return StreamSupport
                    .stream(linkRepository.findAll().spliterator(), false)
                    .map(linkMapper::linkToLinkDTO)
                    .collect(Collectors.toSet());
        }

        Account account = accountCrudService.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                ExceptionMessages.getNotFoundException(username)
                        )
                );

        return account
                .getLinks()
                .stream()
                .map(linkMapper::linkToLinkDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<LinkDTO> findByID(@NonNull Long id) {
        return linkRepository
                .findById(id)
                .map(linkMapper::linkToLinkDTO);
    }

    @Override
    public Optional<LinkDTO> saveLinkDTO(@NonNull LinkRequest linkRequest, Account account) {
        log.debug("logging service: @saveLinkDTO => name : " + linkRequest.getName());

        Link savedLink = null;
        // if POST request was send
        if (Objects.isNull(linkRequest.getId())) {
            Link attachLink = new Link();
            attachLink.setName(linkRequest.getName());
            attachLink.setAccount(account);

            savedLink = linkRepository.save(attachLink);

            account.getLinks().add(savedLink);
            accountCrudService.saveAccount(account);
        } else {
            // if PUT request was send
            Link foundLink = linkRepository
                    .findById(linkRequest.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    ExceptionMessages.getNotFoundException(
                                            linkRequest.getId().toString()
                                    )
                            )
                    );

            foundLink.setName(linkRequest.getName());
            savedLink = linkRepository.save(foundLink);
        }
        return Optional.ofNullable(linkMapper.linkToLinkDTO(savedLink));
    }

    @Override
    public void softDeleteByIdAccount(@NonNull Long id) {
        log.debug("logging service: @softDeleteByIdAccount => id : " + id);

        linkRepository.deleteById(id);
    }
}
