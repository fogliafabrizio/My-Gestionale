package it.fogliafabrizio.mygestionale.repository;

import it.fogliafabrizio.mygestionale.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventsRepository extends JpaRepository<Events, Long> {
}
