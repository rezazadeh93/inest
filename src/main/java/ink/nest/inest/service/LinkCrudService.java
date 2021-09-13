package ink.nest.inest.service;

import ink.nest.inest.api.v1.model.LinkDTO;
import ink.nest.inest.domain.Account;
import io.micrometer.core.lang.Nullable;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.Set;

public interface LinkCrudService {
    Set<LinkDTO> getAll(@Nullable String username);

    Optional<LinkDTO> findByID(@NonNull Long id);

    Optional<LinkDTO> saveLinkDTO(@NonNull String name, Account account);

    void softDeleteByIdAccount(@NonNull Long id);
}
