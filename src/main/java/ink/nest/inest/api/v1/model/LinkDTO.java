package ink.nest.inest.api.v1.model;

import ink.nest.inest.domain.Social;
import ink.nest.inest.domain.Templates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkDTO {
    private Long id;
    private String name;
    private Set<Social> socials = new HashSet<>();
    private Templates template;
}
