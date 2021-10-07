package ink.nest.inest.api.v1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ink.nest.inest.validation.PostMethod;
import ink.nest.inest.validation.PutMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkDTO {
    public LinkDTO(String name) {
        this.name = name;
    }

    @NotNull(groups = PutMethod.class, message = "{message.id}")
    private Long id;
    private Long accountID;

    @NotBlank(groups = {PostMethod.class, PutMethod.class}, message = "{message.name}")
    private String name;
    private Set<SocialDTO> socials = new HashSet<>();

    @JsonIgnore
    private TemplateDTO template;
}
