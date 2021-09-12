package ink.nest.inest.service;

import ink.nest.inest.api.v1.model.AccountDTO;
import ink.nest.inest.api.v1.request.RegisterRequest;
import ink.nest.inest.domain.Account;

import java.util.Optional;
import java.util.Set;

public interface AccountCrudService {
    Set<AccountDTO> getAll();

    Optional<AccountDTO> findByID(Long id);

    Optional<Account> findByEmail(String email);

    Optional<AccountDTO> saveAccountDTO(RegisterRequest accountDTO);

    AccountDTO registerNewUserAccount(RegisterRequest accountDTO);

    void softDeleteByIdAccount(Long id);
}
