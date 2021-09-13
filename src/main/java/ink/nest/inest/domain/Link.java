package ink.nest.inest.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = {"account"})
@EqualsAndHashCode(exclude = {"account"})

@SQLDelete(sql = "UPDATE link SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
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

    private boolean deleted = Boolean.FALSE;

    private LocalDate createAt;
    private LocalDate updatedAt;
}
