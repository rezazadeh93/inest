package ink.nest.inest.service;

import ink.nest.inest.api.v1.mapper.AccountMapper;
import ink.nest.inest.api.v1.model.AccountDTO;
import ink.nest.inest.domain.Account;
import ink.nest.inest.exception.NotFoundException;
import ink.nest.inest.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
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
    public AccountDTO findByID(Long id) {
        log.debug("logging service: @findByID => id : " + id);

        return accountRepository.findById(id)
                .stream()
                .map(accountMapper::accountToAccountDTO)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Account Not Found, For ID value: " + id));
    }

    @Override
    public AccountDTO saveAccountDTO(AccountDTO accountDTO) {
        log.debug("logging service: @saveAccountDTO => id : " + accountDTO.getId());

        Account detachAccount = accountMapper.accountDTOToAccount(accountDTO);
        Account savedAccount = accountRepository.save(Objects.requireNonNull(detachAccount));

        return accountMapper.accountToAccountDTO(savedAccount);
    }

    @Override
    public void softDeleteByIdAccount(Long id) {
        log.debug("logging service: @softDeleteByIdAccount => id : " + id);

        accountRepository.deleteById(id);
    }
}
