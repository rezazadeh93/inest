package ink.nest.inest.domain.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "images")
public class ImageFile {
    @Id
    private final UUID uid = UUID.randomUUID();
    private String name;
    private String imageType;
    private Long size;
    private String imageUrl;
    private Long accountID;
}
