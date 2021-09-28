package ink.nest.inest.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkDTO {
    public LinkDTO(String name) {
        this.name = name;
    }

    private Long id;
    private Long accountID;

    @NotBlank(message = "{message.name}")
    private String name;
    private Set<SocialDTO> socials = new HashSet<>();
    private TemplateDTO template;
}
