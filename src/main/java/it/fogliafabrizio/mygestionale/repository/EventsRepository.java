package it.fogliafabrizio.mygestionale.repository;

import it.fogliafabrizio.mygestionale.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Calendar;
import java.util.List;

public interface EventsRepository extends JpaRepository<Events, Long> {

    public List<Events> findByDate(Calendar date);
}
