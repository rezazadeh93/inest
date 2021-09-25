package ink.nest.inest.service;

import ink.nest.inest.domain.Link;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.Set;

public interface LinkCrudService {
    Set<Link> getAllByUsername(@Nullable String username);

    Optional<Link> findLinkByID(@NonNull Long id);

    Optional<Link> saveLinkByAccount(@NonNull Link linkToSave);

    void softDeleteByID(@NonNull Long id);
}
