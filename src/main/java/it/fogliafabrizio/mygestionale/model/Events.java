package it.fogliafabrizio.mygestionale.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "events"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Events {

    @Id
    @SequenceGenerator(
            name = "events_seq",
            sequenceName = "events_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "events_seq"
    )
    @Column(
            name = "event_id"
    )
    private Long id;

    @Column(
            nullable = false,
            length = 64
    )
    private String name;

    @Lob
    @Column(
            length = 255,
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name = "date",
            nullable = false
    )
    @Temporal(TemporalType.DATE)
    private Calendar date;

    @Column(
            name = "begin_hour",
            nullable = false
            )
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginHour;

    @Column(
            name = "end_hour",
            nullable = false
            )
    @Temporal(TemporalType.TIMESTAMP)
    private Date endHour;


    @Column(
            name = "event_visibility",
            nullable = false
    )
    @Enumerated(
            value = EnumType.STRING
    )
    private Visibility visibility;

    @Column(
            name = "all_user_invitated",
            nullable = false
    )
    private boolean allUserInvitated;

    @ManyToOne
    @JoinColumn(name = "user_owner", nullable = false)
    private Users userCreator;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "events_users_invitated",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Users> invitedUsers = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "events_teams_invitated",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<UserGroups> invitedGroups = new ArrayList<>();

}
