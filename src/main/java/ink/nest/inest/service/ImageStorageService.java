package ink.nest.inest.service;

import ink.nest.inest.api.v1.model.ImageFileDTO;
import ink.nest.inest.api.v1.request.ImageUploadRequest;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageStorageService {
    ImageFileDTO saveImage(@NonNull MultipartFile file, @NonNull String name);

    Resource loadImage(@NonNull UUID uid);
}
