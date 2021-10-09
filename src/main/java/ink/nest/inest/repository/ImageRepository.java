package ink.nest.inest.repository;

import ink.nest.inest.domain.mongodb.ImageFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.rmi.server.UID;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends MongoRepository<ImageFile, UID> {
//    @Query("{ '_id' : ?0 }")
    Optional<ImageFile> findImageFileByUid(UUID uid);
}