package ink.nest.inest.service;

import ink.nest.inest.api.v1.model.OtherLinkDTO;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.Set;

public interface OtherLinkCrudService {
    Set<OtherLinkDTO> getAll();

    Optional<OtherLinkDTO> findByName(@NonNull final String name);

    Optional<OtherLinkDTO> saveOtherLink(@NonNull final OtherLinkDTO otherLink);

    Optional<OtherLinkDTO> updateOtherLink(@NonNull final OtherLinkDTO otherLink);

    void deleteByName(@NonNull final String name);
}
