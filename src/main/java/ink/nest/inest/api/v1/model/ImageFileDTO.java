package ink.nest.inest.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageFileDTO {
    private UUID uid;
    private String name;
    private String imageType;
    private Long size;
    private String imageUrl;
    private Long accountID;
}
