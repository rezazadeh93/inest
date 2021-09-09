package ink.nest.inest.repository;

import ink.nest.inest.domain.Account;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
    Optional<Account> findAccountByEmail(String email);
}
