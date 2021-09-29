package ink.nest.inest.service;

import ink.nest.inest.domain.OtherLink;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.Set;

public interface OtherLinkCrudService {
    Set<OtherLink> getAll();

    Optional<OtherLink> findByName(@NonNull final String name);

    Optional<OtherLink> saveOtherLink(@NonNull final OtherLink otherLink);

    Optional<OtherLink> updateOtherLink(@NonNull final OtherLink otherLink);

    void deleteByName(@NonNull final String name);
}
