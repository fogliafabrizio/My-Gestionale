package it.fogliafabrizio.mygestionale.resporitory;

import it.fogliafabrizio.mygestionale.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventsRepository extends JpaRepository<Events, Long> {
}
