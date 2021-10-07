package ink.nest.inest.service;

import ink.nest.inest.api.v1.model.ImageFileDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    ImageFileDTO saveImage(MultipartFile file);

    Resource loadImage(String uid);
}
