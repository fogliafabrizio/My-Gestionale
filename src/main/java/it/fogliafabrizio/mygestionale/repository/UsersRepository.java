package it.fogliafabrizio.mygestionale.repository;

import it.fogliafabrizio.mygestionale.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    public Users findByEmail(String email);

    public boolean existsByEmail(String email);

    public Users findByPassword(String password);

    public Optional<Users> findById(Long id);
}
