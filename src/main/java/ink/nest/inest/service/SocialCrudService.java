package ink.nest.inest.service;

import ink.nest.inest.domain.Social;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.Set;

public interface SocialCrudService {
    Set<Social> getAllByLinkID(@NonNull final Long linkID);

    Optional<Social> findByIdAndLinkID(@NonNull final Long id, @NonNull Long linkID);

    Optional<Social> findByNameAndLinkID(@NonNull final String name, @NonNull Long linkID);

    Optional<Social> saveBySocial(@NonNull final Social social);

    Optional<Social> updateBySocial(@NonNull final Social social);

    void softDeleteByID(@NonNull final Long id, @NonNull Long linkID);
}
