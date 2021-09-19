package ink.nest.inest.service;

import ink.nest.inest.api.v1.model.LinkDTO;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.Set;

public interface LinkCrudService {
    Set<LinkDTO> getAll(@Nullable String username);

    Optional<LinkDTO> findByID(@NonNull Long id);

    Optional<LinkDTO> saveLinkDtoByAccount(@NonNull LinkDTO linkRequest, @NonNull String accountEmail);

    Optional<LinkDTO> saveLinkDtoByAccount(@NonNull LinkDTO linkRequest, @NonNull Long accountID);

    void softDeleteByID(@NonNull Long id);
}
