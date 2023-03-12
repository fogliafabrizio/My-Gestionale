package it.fogliafabrizio.mygestionale.repository;

import it.fogliafabrizio.mygestionale.model.UserGroups;
import it.fogliafabrizio.mygestionale.model.Users;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserGroupsRepository extends JpaRepository<UserGroups, Long> {

    //  public List<UserGroups> findByUserAdmin (Users userAdmin);
    //  SELECT

    public List<UserGroups> findByUserAdmin (Users userAdmin);

    @Query("SELECT g FROM UserGroups g WHERE g.visibility = it.fogliafabrizio.mygestionale.model.Visibility.PUBLIC OR g.userAdmin = :user")
    List<UserGroups> findPublicOrAdminGroups(@Param("user") Users user);
}
