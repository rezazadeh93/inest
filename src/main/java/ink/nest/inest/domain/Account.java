package ink.nest.inest.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"links"})
@EqualsAndHashCode(exclude = {"links"}, callSuper = false)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastname;

    private String email;
    private String password;

    private int verifyCode;
    private LocalDate verifiedAt;
    private int verifyCount = 0;
    private boolean isVerified = false;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Link> links = new HashSet<>();

    private LocalDate createAt;
    private LocalDate updatedAt;

    @Lob
    private Set<OtherLink>  otherLink = new HashSet<>();

    @Enumerated(value = EnumType.STRING)
    private Permission permission = Permission.USER;
}
