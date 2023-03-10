package it.fogliafabrizio.mygestionale.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "teams"
)
public class UserGroups {

    @Id
    @SequenceGenerator(
            name = "teams_seq",
            sequenceName = "teams_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "teams_seq"
    )
    @Column(
            name = "team_id"
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false,
            length = 128
    )
    private String name;

    @Column(
            name = "team_visibility",
            nullable = false
    )
    @Enumerated(
            value = EnumType.STRING
    )
    private Visibility visibility;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "user_admin_id", nullable = false)
    @JsonIgnore
    private Users userAdmin;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "teams_members",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Users> userMembers = new ArrayList<>();

    /*@ManyToMany(mappedBy = "invitedGroups", cascade = CascadeType.ALL)
    private List<Events> invitedEvents = new ArrayList<>();*/

}
