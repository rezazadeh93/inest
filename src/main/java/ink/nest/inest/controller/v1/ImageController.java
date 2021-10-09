package ink.nest.inest.controller.v1;

import ink.nest.inest.api.v1.model.ImageFileDTO;
import ink.nest.inest.constant.InestApiConstant;
import ink.nest.inest.service.ImageStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(InestApiConstant.API_V1_PATH)
public class ImageController {
    private final ImageStorageService imageStorageService;

    public ImageController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    @GetMapping("/image/{uid}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Resource> getImage(@PathVariable("uid") UUID uid, HttpServletRequest request) {
        Resource resource = imageStorageService.loadImage(uid);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.error("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"%s\"", resource.getFilename())
                ).body(resource);

    }

    @PostMapping("/image")
    @ResponseStatus(HttpStatus.CREATED)
    ImageFileDTO saveNewImage(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
        return imageStorageService.saveImage(file, name);
    }
}
