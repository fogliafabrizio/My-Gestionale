package it.fogliafabrizio.mygestionale.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;


import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private String description = "";

    @Column(
            name = "location"
    )
    private String location = "";

    @Column(
            name = "link"
    )
    private String link = "";

    @Column(
            name = "date",
            nullable = false
    )
    @Temporal(TemporalType.DATE)
    private Calendar date;

    @Column(
            name = "begin_hour"
            )
    @Temporal(TemporalType.TIME)
    private LocalTime beginHour;

    @Column(
            name = "end_hour"
            )
    @Temporal(TemporalType.TIME)
    private LocalTime endHour;

    @Column(
            name = "all_day"
    )
    private boolean allDay = false;


    @Column(
            name = "event_visibility",
            nullable = false
    )
    @Enumerated(
            value = EnumType.STRING
    )
    private Visibility visibility;

    @Column(
            name = "all_user_invitated"
    )
    private boolean allUserInvitated = false;

    @Column(
            name = "festivity"
    )
    private boolean festivity = false;


    @Column(
            name = "dob"
    )
    private boolean dob = false;

    @UpdateTimestamp
    private LocalDateTime update_on;

    @ManyToOne
    @JoinColumn(name = "user_owner")
    @JsonIgnore
    private Users userCreator;

    @ManyToMany
    @JoinTable(
            name = "events_users_invitated",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private List<Users> invitedUsers = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "events_teams_invitated",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    @JsonIgnore
    private List<UserGroups> invitedGroups = new ArrayList<>();

    @Override
    public String toString() {
        return "Events{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", link='" + link + '\'' +
                ", date=" + date +
                ", beginHour=" + beginHour +
                ", endHour=" + endHour +
                ", allDay=" + allDay +
                ", visibility=" + visibility +
                ", allUserInvitated=" + allUserInvitated +
                ", festivity=" + festivity +
                ", update_on=" + update_on + '}';
    }
}
