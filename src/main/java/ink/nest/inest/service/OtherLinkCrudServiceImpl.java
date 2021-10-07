package ink.nest.inest.service;

import ink.nest.inest.api.v1.model.OtherLinkDTO;
import ink.nest.inest.domain.Account;
import ink.nest.inest.exception.InternalServerException;
import ink.nest.inest.exception.ResourceExistsException;
import ink.nest.inest.exception.ResourceNotFoundException;
import ink.nest.inest.utility.Messages;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OtherLinkCrudServiceImpl implements OtherLinkCrudService {
    private final AccountCrudService accountCrudService;
    private final Messages messages;
    private final ActiveUserService activeUserService;

    public OtherLinkCrudServiceImpl(AccountCrudService accountCrudService,
                                    Messages messages,
                                    ActiveUserService activeUserService) {
        this.accountCrudService = accountCrudService;
        this.activeUserService = activeUserService;
        this.messages = messages;
    }

    @Override
    public Set<OtherLinkDTO> getAll() {
        return activeUserService.currentAccount().getOtherLinks();
    }

    @Override
    public Optional<OtherLinkDTO> findByName(@NonNull final String name) {
        return activeUserService.currentAccount()
                .getOtherLinks()
                .stream()
                .filter(otherLink -> {
                    System.out.println(otherLink.getName());
                    return otherLink.getName().equals(name);
                })
                .findFirst();
    }

    @Override
    public Optional<OtherLinkDTO> saveOtherLink(@NonNull final OtherLinkDTO otherLink) {
        // find account
        Account accountFound = activeUserService.currentAccount();

        if (accountFound.getOtherLinks()
                .stream()
                .anyMatch(elm -> elm.getName().equals(otherLink.getName()))) {
            throw new ResourceExistsException(
                    messages.getExceptionMessage("message.resourceAlreadyExist",
                            List.of(otherLink.getName()))
            );
        }

        OtherLinkDTO otherLinkToSave = new OtherLinkDTO(
                otherLink.getName(),
                otherLink.getLabel(),
                otherLink.getUrl()
        );

        accountFound.getOtherLinks().add(otherLinkToSave);

        return saveAccount(otherLink, accountFound);
    }

    @Override
    public Optional<OtherLinkDTO> updateOtherLink(@NonNull final OtherLinkDTO otherLink) {
        //find account
        Account accountFound = activeUserService.currentAccount();

        if (accountFound.getOtherLinks()
                .stream()
                .noneMatch(elm -> elm.getName().equals(otherLink.getName()))) {
            throw new ResourceNotFoundException(
                    messages.getExceptionMessage(
                            "message.notFound",
                            List.of(otherLink.getName())
                    )
            );
        }

        accountFound.getOtherLinks()
                .forEach(elm -> {
                    if (elm.getName().equals(otherLink.getName())) {
                        elm.setLabel(otherLink.getLabel());
                        elm.setUrl(otherLink.getUrl());
                    }
                });

        return saveAccount(otherLink, accountFound);
    }

    @Override
    public void deleteByName(@NonNull final String name) {
        Account accountFound = activeUserService.currentAccount();

        if (!accountFound.getOtherLinks()
                .removeIf(otherLink -> otherLink.getName().equals(name)))
            throw new ResourceNotFoundException(
                    messages.getExceptionMessage(
                            "message.notFound",
                            List.of(name)
                    )
            );

        accountCrudService.saveAccount(accountFound)
                .orElseThrow(() -> new InternalServerException(
                        messages.getExceptionMessage("message.internalServerError",
                                List.of(name))
                ));
    }

    private Optional<OtherLinkDTO> saveAccount(@NonNull OtherLinkDTO otherLink, Account accountFound) {
        return accountCrudService.saveAccount(accountFound)
                .orElseThrow(() -> new InternalServerException(
                                messages.getExceptionMessage("message.internalServerError",
                                        List.of(otherLink.getName()))
                        )
                ).getOtherLinks()
                .stream().filter(elm -> elm.getName().equals(otherLink.getName()))
                .findFirst();
    }
}
