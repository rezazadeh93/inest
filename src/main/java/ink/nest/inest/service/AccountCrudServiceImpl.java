package ink.nest.inest.service;

import ink.nest.inest.api.v1.mapper.AccountMapper;
import ink.nest.inest.api.v1.request.RegisterRequest;
import ink.nest.inest.domain.Account;
import ink.nest.inest.exception.ResourceExistsException;
import ink.nest.inest.exception.ResourceNotFoundException;
import ink.nest.inest.repository.AccountRepository;
import ink.nest.inest.utility.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AccountCrudServiceImpl implements AccountCrudService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final Messages messages;

    public AccountCrudServiceImpl(AccountRepository accountRepository,
                                  AccountMapper accountMapper,
                                  Messages messages) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.messages = messages;
    }

    @Override
    public Set<Account> getAll() {
        log.debug("logging service: @getAll method");

        return Collections.unmodifiableSet((Set<Account>) accountRepository.findAll());
    }

    @Override
    public Optional<Account> findByID(Long id) {
        log.debug("logging service: @findByID => id : " + id);

        return accountRepository.findById(id);
    }

    @Override
    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    @Override
    public Optional<Account> saveAccount(Account account) {
        log.debug("logging service: @saveAccountDTO => email : " + account.getEmail());

        Account savedAccount;
        // if request was send POST
        if (Objects.isNull(account.getId())) {
            savedAccount = accountRepository.save(Objects.requireNonNull(account));

        } else {
            // if request was send PUT
            accountRepository.findById(account.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                                    messages.getExceptionMessage(
                                            "message.notFound",
                                            Collections.singletonList(account.getId())
                                    )
                            )
                    );

            savedAccount = accountRepository.save(account);
        }

        return Optional.of(savedAccount);
    }

    @Override
    public void softDeleteByAccountID(Long id) {
        log.debug("logging service: @softDeleteByIdAccount => id : " + id);

        accountRepository.deleteById(id);
    }

    @Override
    public Optional<Account> registerNewUserAccount(RegisterRequest request) {

        if (emailExist(request.getEmail())) {
            throw new ResourceExistsException(
                    messages.getExceptionMessage("message.resourceAlreadyExist", List.of(request.getEmail()))
            );
        }

        Account detachAccount = accountMapper.dtoReqRegisterToAccount(request);

        return saveAccount(detachAccount);
    }

    private boolean emailExist(String email) {
        return findAccountByEmail(email).isPresent();
    }
}
