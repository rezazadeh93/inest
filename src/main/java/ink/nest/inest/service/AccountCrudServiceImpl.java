package ink.nest.inest.service;

import ink.nest.inest.api.v1.mapper.AccountMapper;
import ink.nest.inest.api.v1.model.AccountDTO;
import ink.nest.inest.api.v1.request.RegisterRequest;
import ink.nest.inest.domain.Account;
import ink.nest.inest.domain.Link;
import ink.nest.inest.exception.EmailExistsException;
import ink.nest.inest.exception.ExceptionMessages;
import ink.nest.inest.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class AccountCrudServiceImpl implements AccountCrudService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountCrudServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Set<AccountDTO> getAll() {
        log.debug("logging service: @getAll method");

        return StreamSupport.stream(accountRepository.findAll().spliterator(), false)
                .map(accountMapper::accountToAccountDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<AccountDTO> findByID(Long id) {
        log.debug("logging service: @findByID => id : " + id);

        return accountRepository.findById(id)
                .stream()
                .map(accountMapper::accountToAccountDTO)
                .findFirst();
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    @Override
    public Optional<AccountDTO> saveAccount(Account account) {
        log.debug("logging service: @saveAccountDTO => email : " + account.getEmail());

        Account savedAccount = null;
        // if request was send POST
        if (Objects.isNull(account.getId())) {
            savedAccount = accountRepository.save(Objects.requireNonNull(account));

        } else {
            // if request was send PUT
            accountRepository.findById(account.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    ExceptionMessages.getNotFoundException(
                                            account.getId().toString()
                                    )
                            )
                    );

            savedAccount = accountRepository.save(account);
        }

        return Optional.ofNullable(accountMapper.accountToAccountDTO(savedAccount));
    }

    @Override
    public void softDeleteByIdAccount(Long id) {
        log.debug("logging service: @softDeleteByIdAccount => id : " + id);

        accountRepository.deleteById(id);
    }

    @Override
    public AccountDTO registerNewUserAccount(RegisterRequest request) {

        if (emailExist(request.getEmail())) {
            throw new EmailExistsException("This email already exist: " + request.getEmail());
        }

        Account detachAccount = accountMapper.dtoReqRegisterToAccount(request);

        return saveAccount(detachAccount)
                .orElse(null);
    }

    private boolean emailExist(String email) {
        return findByEmail(email).isPresent();
    }
}
