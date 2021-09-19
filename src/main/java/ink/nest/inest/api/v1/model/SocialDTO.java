package ink.nest.inest.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialDTO {
    private Long id;

    // @Todo This field must be Validate too
    // @NotBlank(message = "{validation.required}")
    private Long linkID;

    // @Todo validate if name is in socialName tables or not
    @NotBlank(message = "{validation.required}")
    private String name;

    @NotBlank(message = "{validation.required}")
    private String label;

    @URL
    @NotBlank(message = "{validation.required}")
    private String url;
}
