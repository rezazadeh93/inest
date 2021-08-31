package ink.nest.inest.repository;

import ink.nest.inest.domain.Account;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
}
