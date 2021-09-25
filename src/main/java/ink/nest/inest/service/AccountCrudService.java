package ink.nest.inest.service;

import ink.nest.inest.api.v1.request.RegisterRequest;
import ink.nest.inest.domain.Account;

import java.util.Optional;
import java.util.Set;

public interface AccountCrudService {
    Set<Account> getAll();

    Optional<Account> findByID(Long id);

    Optional<Account> findAccountByEmail(String email);

    Optional<Account> saveAccount(Account account);

    Optional<Account> registerNewUserAccount(RegisterRequest account);

    void softDeleteByAccountID(Long id);
}
