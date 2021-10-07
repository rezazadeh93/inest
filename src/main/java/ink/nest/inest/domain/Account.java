package ink.nest.inest.domain;

import ink.nest.inest.api.v1.model.OtherLinkDTO;
import ink.nest.inest.convertor.HashSetConverter;
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
@EqualsAndHashCode(exclude = {"links"}, callSuper = false)
@ToString(exclude = {"links"})

@Entity
@Table(name = "account")
@SQLDelete(sql = "UPDATE account SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastname;

    @Column(unique = true)
    private String email;
    private String passwordEncrypted;

    private int verifyCode;
    private LocalDate verifiedAt;
    private int verifyCount = 0;
    private boolean isVerified = false;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Link> links = new HashSet<>();

    @Column(columnDefinition = "text")
    @Convert(converter = HashSetConverter.class)
    private Set<OtherLinkDTO> otherLinks = new HashSet<>();

    @Enumerated(value = EnumType.STRING)
    private Permission permission = Permission.USER;

    private boolean deleted = Boolean.FALSE;

    private LocalDate createAt;
    private LocalDate updatedAt;
}
