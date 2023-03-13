package it.fogliafabrizio.mygestionale.repository;

import it.fogliafabrizio.mygestionale.model.Events;
import it.fogliafabrizio.mygestionale.model.UserGroups;
import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.model.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.List;

public interface EventsRepository extends JpaRepository<Events, Long> {

    @Query("SELECT e FROM Events e WHERE e.date = :date AND (e.visibility = :visibility OR e.allUserInvitated = :allUserInvitated OR e.festivity = :festivity)")
    public List<Events> findByDateAndVisibilityOrAllUserInvitatedOrFestivity(@Param("date") Calendar date, @Param("visibility") Visibility visibility, @Param("allUserInvitated") boolean allUserInvitated, @Param("festivity") boolean festivity);

    public List<Events> findByUserCreator(Users users);

    public List<Events> findByDateAndUserCreator(Calendar data, Users users);

    @Query("SELECT e FROM Events e JOIN e.invitedUsers u WHERE u = :user")
    public List<Events> findByInvitedUser(@Param("user") Users user);

    @Query("SELECT e FROM Events e JOIN e.invitedUsers u WHERE u = :user AND e.date = :calendarDate")
    public List<Events> findByInvitedUserAndDate(@Param("user") Users user, @Param("calendarDate") Calendar calendarDate);

    @Query("SELECT e FROM Events e JOIN e.invitedGroups g WHERE g IN :groups")
    public List<Events> findByInvitedGroups(@Param("groups") List<UserGroups> groups);

    @Query("SELECT e FROM Events e JOIN e.invitedGroups g WHERE g IN :groups AND e.date = :date")
    public List<Events> findByInvitedGroupsAndDate(@Param("groups") List<UserGroups> groups, @Param("date") Calendar date);


}
