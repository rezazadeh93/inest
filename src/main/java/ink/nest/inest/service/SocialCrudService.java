package ink.nest.inest.service;

import ink.nest.inest.api.v1.model.SocialDTO;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.Set;

public interface SocialCrudService {
    Set<SocialDTO> getAllByLinkID(@NonNull Long linkID);

    Optional<SocialDTO> findByIdAndLinkID(@NonNull Long id, @NonNull Long linkID);

    Optional<SocialDTO> saveBySocialDTO(@NonNull SocialDTO socialDTO, @NonNull Long linkID);

    void softDeleteByID(@NonNull Long id, @NonNull Long linkID);
}
