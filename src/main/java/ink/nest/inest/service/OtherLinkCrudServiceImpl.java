package ink.nest.inest.service;

import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.domain.Account;
import ink.nest.inest.domain.OtherLink;
import ink.nest.inest.exception.InternalServerException;
import ink.nest.inest.exception.ResourceExistsException;
import ink.nest.inest.exception.ResourceNotFoundException;
import ink.nest.inest.security.JwtTokenUtil;
import ink.nest.inest.utility.Messages;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class OtherLinkCrudServiceImpl implements OtherLinkCrudService {
    private final AccountCrudService accountCrudService;
    private final HttpServletRequest request;
    private final JwtTokenUtil jwtTokenUtil;
    private final Messages messages;

    public OtherLinkCrudServiceImpl(AccountCrudService accountCrudService,
                                    HttpServletRequest request,
                                    JwtTokenUtil jwtTokenUtil, Messages messages) {
        this.accountCrudService = accountCrudService;
        this.request = request;
        this.jwtTokenUtil = jwtTokenUtil;
        this.messages = messages;
    }

    @Override
    public Set<OtherLink> getAll() {
        return getAccount().getOtherLinks();
    }

    @Override
    public Optional<OtherLink> findByName(@NonNull final String name) {
        return getAccount()
                .getOtherLinks()
                .stream()
                .filter(otherLink -> otherLink.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<OtherLink> saveOtherLink(@NonNull final OtherLink otherLink) {
        // find account
        Account accountFound = getAccount();

        if (accountFound.getOtherLinks()
                .stream()
                .anyMatch(elm -> elm.getName().equals(otherLink.getName()))) {
            throw new ResourceExistsException(
                    messages.getExceptionMessage("message.resourceAlreadyExist",
                            List.of(otherLink.getName()))
            );
        }

        OtherLink otherLinkToSave = new OtherLink(
                otherLink.getName(),
                otherLink.getLabel(),
                otherLink.getUrl()
        );

        accountFound.getOtherLinks().add(otherLinkToSave);

        return saveAccount(otherLink, accountFound);
    }

    @Override
    public Optional<OtherLink> updateOtherLink(@NonNull final OtherLink otherLink) {
        //find account
        Account accountFound = getAccount();

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
        Account accountFound = getAccount();

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

    private Optional<OtherLink> saveAccount(@NonNull OtherLink otherLink, Account accountFound) {
        return accountCrudService.saveAccount(accountFound)
                .orElseThrow(() -> new InternalServerException(
                                messages.getExceptionMessage("message.internalServerError",
                                        List.of(otherLink.getName()))
                        )
                ).getOtherLinks()
                .stream().filter(elm -> elm.getName().equals(otherLink.getName()))
                .findFirst();
    }

    private Account getAccount() {
        //get token from header
        String token = Objects.requireNonNull(request.getHeader(InestApiConstant.HEADER_AUTHORIZATION))
                .replace(InestApiConstant.HEADER_BEARER, "");

        String username = jwtTokenUtil.getUsername(token);

        return accountCrudService.findAccountByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                                messages.getExceptionMessage(
                                        "message.notFound",
                                        Collections.singletonList(username)
                                )
                        )
                );
    }
}
