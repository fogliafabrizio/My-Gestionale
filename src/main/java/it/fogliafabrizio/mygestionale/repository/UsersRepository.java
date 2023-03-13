package it.fogliafabrizio.mygestionale.repository;

import it.fogliafabrizio.mygestionale.model.UserGroups;
import it.fogliafabrizio.mygestionale.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    public Users findByEmail(String email);

    public boolean existsByEmail(String email);

    public Users findByPassword(String password);

    public Optional<Users> findById(Long id);

    @Query("SELECT u FROM Users u WHERE MONTH(u.dateOfBirthday) = :month AND DAY(u.dateOfBirthday) = :day")
    public List<Users> findByBirthdateMonthAndDay(@Param("month") int month, @Param("day") int day);


}
