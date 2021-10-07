package ink.nest.inest.service;

import ink.nest.inest.api.v1.mapper.ImageMapper;
import ink.nest.inest.api.v1.model.ImageFileDTO;
import ink.nest.inest.domain.mongodb.ImageFile;
import ink.nest.inest.exception.FileStorageException;
import ink.nest.inest.exception.InternalServerException;
import ink.nest.inest.exception.ResourceNotFoundException;
import ink.nest.inest.repository.ImageRepository;
import ink.nest.inest.utility.Messages;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {
    private final ImageRepository imageRepository;
    private final ActiveUserService activeUserService;
    private final Messages messages;
    private final ImageMapper imageMapper;

    private final Path fileStorageLocation;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ImageStorageServiceImpl(ImageRepository imageRepository,
                                ActiveUserService activeUserService,
                                Messages messages,
                                ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.activeUserService = activeUserService;
        this.messages = messages;
        this.imageMapper = imageMapper;
        this.fileStorageLocation = Path.of(uploadDir)
                .toAbsolutePath()
                .normalize();


        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException(messages.getExceptionMessage("file.cannotCreateDir"), ex);
        }
    }

    @Override
    public ImageFileDTO saveImage(MultipartFile file) {
        // Normalize file name
        String imageName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension;

        // Check if the file's name contains invalid characters
        if (imageName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + imageName);
        }

        try {
            fileExtension = imageName.substring(imageName.lastIndexOf("."));
        } catch (Exception ex) {
            throw new FileStorageException(
                    messages.getExceptionMessage(
                            "file.imageNameNotValid",
                            imageName
                    )
            );
        }

        ImageFile newImage = new ImageFile();
        newImage.setName(imageName);
        newImage.setImageType(file.getContentType());
        newImage.setSize(file.getSize());
        newImage.setAccountID(activeUserService.currentAccount().getId());

        try {
            Path target = this.fileStorageLocation.resolve(newImage.getUid() + fileExtension);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            newImage.setImageUrl(target.toString());

            return imageMapper.imageToImageDTO(imageRepository.save(newImage));

        } catch (IOException ex) {
            throw new InternalServerException(messages.getExceptionMessage("file.cannotStoreImage"), ex);
        } catch (Exception ex) {
            throw new InternalServerException(messages.getExceptionMessage(
                    "message.internalServerError",
                    file.toString()
            ));
        }
    }

    @Override
    public Resource loadImage(String uid) {
        String imageURL = imageRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.getExceptionMessage("message.notFound",
                                List.of(uid)
                        ))).getImageUrl();

        try {
            Path filePath = this.fileStorageLocation.resolve(imageURL).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException(messages.getExceptionMessage("message.notFound", uid));
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException(messages.getExceptionMessage("message.notFound", uid), ex);
        }
    }
}
