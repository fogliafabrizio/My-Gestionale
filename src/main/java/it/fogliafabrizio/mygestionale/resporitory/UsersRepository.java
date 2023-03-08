package it.fogliafabrizio.mygestionale.resporitory;

import it.fogliafabrizio.mygestionale.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

    public Users findByEmail(String email);
}
