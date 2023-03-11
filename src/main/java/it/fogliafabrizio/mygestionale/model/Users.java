package it.fogliafabrizio.mygestionale.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "users"
)
public class Users {

    @Id
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_seq"
    )
    private Long id;

    @Column(
            name = "first_name",
            nullable = false,
            length = 64
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            length = 64
    )
    private String lastName;

    @Column(
            name = "email",
            nullable = false,
            length = 128,
            unique = true
    )
    private String email;

    @Column(
            name = "psw",
            nullable = false,
            length = 255
    )
    private String password;

    @Column(
            name = "role",
            nullable = false
    )
    @Enumerated(
            value = EnumType.STRING
    )
    private Role role;

    @Column(
            name = "enabled"
    )
    private boolean enabled=false;

    @Column(
            name = "dob"
    )
    @Temporal(TemporalType.DATE)
    private Calendar dateOfBirthday;

    @CreationTimestamp
    private LocalDateTime created_on;

    @UpdateTimestamp
    private LocalDateTime update_on;

    @OneToMany(mappedBy = "userCreator")
    private List<Events> events = new ArrayList<>();

    @ManyToMany(mappedBy = "invitedUsers")
    private List<Events> invitedEvents = new ArrayList<>();

    @OneToMany(mappedBy = "userAdmin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGroups> groups = new ArrayList<>();

    @ManyToMany(mappedBy = "userMembers")
    private List<UserGroups> teamsUsers = new ArrayList<>();

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", enabled=" + enabled +
                ", dateOfBirthday=" + dateOfBirthday +
                ", created_on=" + created_on +
                ", update_on=" + update_on + '}';
    }
}
