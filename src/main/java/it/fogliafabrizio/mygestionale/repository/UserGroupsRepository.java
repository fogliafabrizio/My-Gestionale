package it.fogliafabrizio.mygestionale.repository;

import it.fogliafabrizio.mygestionale.model.UserGroups;
import it.fogliafabrizio.mygestionale.model.Users;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGroupsRepository extends JpaRepository<UserGroups, Long> {

    //  public List<UserGroups> findByUserAdmin (Users userAdmin);
    //  SELECT

    public List<UserGroups> findByUserAdmin (Users userAdmin);
}
