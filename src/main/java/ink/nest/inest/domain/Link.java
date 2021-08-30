package ink.nest.inest.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = {"account"})
@EqualsAndHashCode(exclude = {"account"})
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Social> socials = new HashSet<>();

    @ManyToOne
    private Account account;

    @OneToOne(cascade = CascadeType.DETACH)
    private Templates template;
}
