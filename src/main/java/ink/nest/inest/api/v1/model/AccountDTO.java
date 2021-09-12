package ink.nest.inest.api.v1.model;

import ink.nest.inest.domain.Link;
import ink.nest.inest.domain.OtherLink;
import ink.nest.inest.domain.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    public AccountDTO(String email) {
        this.email = email;
    }

    private Long id = null;

    private String firstName;
    private String lastname;
    private String email;
    private boolean isVerified;

    private Set<Link> links = new HashSet<>();
    private Set<OtherLink> otherLinks = new HashSet<>();

    private Permission permission;
    private LocalDate createAt;
}
