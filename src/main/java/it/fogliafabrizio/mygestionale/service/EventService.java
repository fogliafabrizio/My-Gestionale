package it.fogliafabrizio.mygestionale.service;

import it.fogliafabrizio.mygestionale.model.EventRequest;
import it.fogliafabrizio.mygestionale.model.Events;

import java.util.List;

public interface EventService {

    public String createEvent(EventRequest eventRequest, Long id);

    public List<Events> generateEvents(Long id, int day, int month, int year);

    public List<Events> generateAllEvents(Long id, int month, int year);

    public String modifyEvent(EventRequest eventRequest, Long id);
}
