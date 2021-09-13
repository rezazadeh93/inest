package ink.nest.inest.repository;

import ink.nest.inest.domain.Account;
import ink.nest.inest.domain.Link;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface LinkRepository extends PagingAndSortingRepository<Link, Long> {
}
