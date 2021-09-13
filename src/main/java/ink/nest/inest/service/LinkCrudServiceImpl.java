package ink.nest.inest.service;

import ink.nest.inest.api.v1.mapper.LinkMapper;
import ink.nest.inest.api.v1.model.LinkDTO;
import ink.nest.inest.domain.Account;
import ink.nest.inest.domain.Link;
import ink.nest.inest.repository.LinkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

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
        Account accountDTO = accountCrudService.findByEmail(username).orElse(null);

        if (Objects.isNull(accountDTO)) {
            return StreamSupport
                    .stream(linkRepository.findAll().spliterator(), false)
                    .map(linkMapper::linkToLinkDTO)
                    .collect(Collectors.toSet());
        }

        return accountDTO
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
    public Optional<LinkDTO> saveLinkDTO(@NonNull String name, Account account) {
        log.debug("logging service: @saveLinkDTO => name : " + name);

        Link attachLink = new Link();
        attachLink.setName(name);
        attachLink.setAccount(account);

         LinkDTO savedLink = linkMapper.linkToLinkDTO(linkRepository.save(attachLink));

        return Optional.ofNullable(savedLink);
    }

    @Override
    public void softDeleteByIdAccount(@NonNull Long id) {
        log.debug("logging service: @softDeleteByIdAccount => id : " + id);

        linkRepository.deleteById(id);
    }
}
