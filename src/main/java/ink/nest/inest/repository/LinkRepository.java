package ink.nest.inest.repository;

import ink.nest.inest.domain.Link;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LinkRepository extends PagingAndSortingRepository<Link, Long> {
}
