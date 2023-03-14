package it.fogliafabrizio.mygestionale.service;

import it.fogliafabrizio.mygestionale.model.EventRequest;
import it.fogliafabrizio.mygestionale.model.Events;
import it.fogliafabrizio.mygestionale.model.Users;

import java.util.List;

public interface EventService {

    public String createEvent(EventRequest eventRequest, Long id, String url);

    public List<Events> generateEvents(Long id, int day, int month, int year);

    public List<Events> generateAllEvents(Long id, int month, int year);

    public String modifyEvent(EventRequest eventRequest, Long id, String url);

    public void sendInvitationMail(Events event, EventRequest eventRequest, String url, boolean modifyEvent);
}
