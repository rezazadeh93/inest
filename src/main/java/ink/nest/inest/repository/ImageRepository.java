package ink.nest.inest.repository;

import ink.nest.inest.domain.mongodb.ImageFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepository extends MongoRepository<ImageFile, String> {
}
