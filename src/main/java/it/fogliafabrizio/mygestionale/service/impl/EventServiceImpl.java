package it.fogliafabrizio.mygestionale.service.impl;

import it.fogliafabrizio.mygestionale.model.*;
import it.fogliafabrizio.mygestionale.repository.EventsRepository;
import it.fogliafabrizio.mygestionale.repository.UserGroupsRepository;
import it.fogliafabrizio.mygestionale.repository.UsersRepository;
import it.fogliafabrizio.mygestionale.service.EventService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserGroupsRepository groupsRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public String createEvent(EventRequest eventRequest, Long id, String url) {
        Events event = new Events();
        // Name
        event.setName(eventRequest.getEventName());
        // Descrizione
        event.setDescription(eventRequest.getEventDescription());
        // Location
        event.setLocation(eventRequest.getEventLocation());
        // Link
        event.setLink(eventRequest.getEventLink());
        // Visibilità
        event.setVisibility(eventRequest.getEventVisibility());
        // Controllo data - non può essere prima di Oggi!
        if (!eventRequest.getEventDate().isBefore(LocalDate.now())) {
            // Data
            Calendar data = Calendar.getInstance();
            data.set(eventRequest.getEventDate().getYear(), eventRequest.getEventDate().getMonthValue() - 1, eventRequest.getEventDate().getDayOfMonth());
            event.setDate(data);
        } else {
            return "BEFORE_TODAY";
        }
        // All day
        event.setAllDay(eventRequest.isAllDayEvent());
        // Se non è All day - set Inizio ora e Fine ora
        if (!eventRequest.isAllDayEvent()) {
            //  Controllo che ora inizio sia prima di ora di fine
            //  Se l'ora di fine è prima dell'ora di inizio
            if (eventRequest.getEventEndTime().isBefore(eventRequest.getEventStartTime())) {
                return "ERR_TIME";
            } else if (eventRequest.getEventStartTime().equals(eventRequest.getEventEndTime())) {
                //  Se l'ora di inizio è uguale a quella di fine
                return "ERR_TIME";
            } else {
                event.setBeginHour(eventRequest.getEventStartTime());
                event.setEndHour(eventRequest.getEventEndTime());
            }
        }
        // All User - In questo modo se un utente nuovo viene creato, non serve che sia inserito tra gli invitati dopo
        event.setAllUserInvitated(eventRequest.isAllUsers());
        //   Se tutti gli utenti sono invitati - non segno gli invitati
        if (!eventRequest.isAllUsers()) {
            List<Long> idUserInvitated = eventRequest.getUserIds();
            List<Users> userInvitated = new ArrayList<>();
            for (Long userId : idUserInvitated) {
                Users user = usersRepository.findById(userId).orElseThrow();
                userInvitated.add(user);
            }
            event.setInvitedUsers(userInvitated);
        }

        // Gruppi invitati
        List<Long> idGroupInvitated = eventRequest.getGroupIds();
        List<UserGroups> groupInvitated = new ArrayList<>();
        for (Long groupId : idGroupInvitated) {
            UserGroups group = groupsRepository.findById(groupId).orElseThrow();
            groupInvitated.add(group);
        }
        event.setInvitedGroups(groupInvitated);
        event.setUserCreator(usersRepository.findById(id).orElseThrow());
        event.setFestivity(false);
        Events eventSave = eventsRepository.save(event);

        sendInvitationMail(eventSave, eventRequest, url, false);
        return "OK";
    }

    @Override
    public void sendInvitationMail(Events event, EventRequest eventRequest, String url, boolean modifyEvent) {
        String from = "info@rationence.eu";
        String subject = "Sei stato invitato ad un nuovo evento!";
        String content = "Ciao [[name]]! <br> Sei stato invitato a questo evento da [[userCreator]]: <br> <h3> [[eventName]] </h3><br> Data e Ora: [[data]] - [[orario]]<br><br> Per vedere maggiori dettagli <a href=\"[[URL]]\" target=\"_self\">Clicca qui</a> <br><br> Rationence.";
        List<Long> idUserInvitated = eventRequest.getUserIds();
        List<Long> idGroupsInvitated = eventRequest.getGroupIds();
        List<String> emailUser = new ArrayList<>();
        content = content.replace("[[userCreator]]", event.getUserCreator().getFirstName() + " " + event.getUserCreator().getLastName());
        content = content.replace("[[eventName]]", event.getName());
        content = content.replace("[[data]]", eventRequest.getEventDate().toString());
        if (event.isAllDay()) {
            content = content.replace("[[orario]]", "Tutto il giorno.");
        } else {
            content = content.replace("[[orario]]", "dalle ore " + eventRequest.getEventStartTime() + " alle ore" + eventRequest.getEventEndTime());
        }

        String siteUrl = url + "/calendar/event/" + event.getId();
        content = content.replace("[[URL]]", siteUrl);

        //  Fare la lista di mail
        for (Long id : idUserInvitated) {
            emailUser.add(usersRepository.findById(id).get().getEmail());
        }
        for (Long id : idGroupsInvitated) {
            List<Users> users = (groupsRepository.findById(id).get().getUserMembers());
            for (Users user : users) {
                String email = user.getEmail();
                if (!emailUser.contains(email) && (!email.equals(event.getUserCreator().getEmail()))) {
                    //  TODO: Dopo metodo creazione gruppi provare se funziona questo per evitare mail doppie
                    emailUser.add(email);
                }
            }
        }
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            for (String to : emailUser) {
                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(subject);
                Users userSaved = usersRepository.findByEmail(to);
                String contentNoName = content;
                contentNoName = contentNoName.replace("[[name]]", userSaved.getFirstName());

                helper.setText(contentNoName, true);

                javaMailSender.send(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Events> generateEvents(Long id, int day, int month, int year) {
        Calendar data = Calendar.getInstance();
        data.set(Calendar.DAY_OF_MONTH, day);
        switch (month) {
            case 1:
                data.set(Calendar.MONTH, Calendar.JANUARY);
                break;
            case 2:
                data.set(Calendar.MONTH, Calendar.FEBRUARY);
                break;
            case 3:
                data.set(Calendar.MONTH, Calendar.MARCH);
                break;
            case 4:
                data.set(Calendar.MONTH, Calendar.APRIL);
                break;
            case 5:
                data.set(Calendar.MONTH, Calendar.MAY);
                break;
            case 6:
                data.set(Calendar.MONTH, Calendar.JUNE);
                break;
            case 7:
                data.set(Calendar.MONTH, Calendar.JULY);
                break;
            case 8:
                data.set(Calendar.MONTH, Calendar.AUGUST);
                break;
            case 9:
                data.set(Calendar.MONTH, Calendar.SEPTEMBER);
                break;
            case 10:
                data.set(Calendar.MONTH, Calendar.OCTOBER);
                break;
            case 11:
                data.set(Calendar.MONTH, Calendar.NOVEMBER);
                break;
            case 12:
                data.set(Calendar.MONTH, Calendar.DECEMBER);
                break;
        }
        data.set(Calendar.YEAR, year);
        /*
            CERCO TUTTI GLI EVENTI PUBLIC, TUTTI GLI UTENTI SONO INVITATI O FESTIVITA'
         */
        List<Events> eventsInThisDayAllUserInvitatedOrFesitivityOrPublic = eventsRepository.findByDateAndVisibilityOrAllUserInvitatedOrFestivity(
                data,
                Visibility.PUBLIC,
                true,
                true
        );
        /*
            CERCO TUTTI GLI EVENTI CREATI DALL'UTENTE
         */
        List<Events> eventsInThisDayUserOwner = eventsRepository.findByDateAndUserCreator(data, usersRepository.findById(id).orElseThrow());

        /*
            CERCO TUTTI GLI EVENTI IN CUI L'UTENTE E' INVITATO
         */
        List<Events> eventsInThisDayUserInvitated = eventsRepository.findByInvitedUserAndDate(usersRepository.findById(id).orElseThrow(), data);
        //System.out.println(eventsUserInvitated);
        /*
            CERCO TUTTI GLI EVENTI IN CUI IL GRUPPO DELL'UTENTE E' INVITATO
         */
        List<UserGroups> groupsMemberUser = groupsRepository.findByMemberUser(usersRepository.findById(id).orElseThrow());
        List<Events> eventsInThisDayGroupsMember = eventsRepository.findByInvitedGroupsAndDate(groupsMemberUser, data);
        /*
            CERCO COMPLEANNI
         */
        List<Users> birthdateUserList = usersRepository.findByBirthdateMonthAndDay(month, day);
        List<Events> birthdayEvents = new ArrayList<>();
        if (!birthdateUserList.isEmpty()) {
            for (Users userBirthdate : birthdateUserList) {
                Events birthdate = new Events();
                birthdate.setDate(data);
                birthdate.setName("Compleanno di " + userBirthdate.getFirstName() + " " + userBirthdate.getLastName());
                birthdate.setVisibility(Visibility.PUBLIC);
                birthdate.setAllDay(true);
                birthdate.setAllUserInvitated(true);
                birthdate.setDob(true);
                birthdate.setDescription("Tanti auguri di buon compleanno " + userBirthdate.getFirstName());
                birthdayEvents.add(birthdate);
            }
        }
        /*
            CREO LA LISTA FINALE ELIMINANDO I DOPPIONI
         */
        List<Events> allEvents = new ArrayList<>();
        for (Events event : eventsInThisDayAllUserInvitatedOrFesitivityOrPublic) {
            if (!allEvents.contains(event)) {
                allEvents.add(event);
            }
        }
        for (Events event : eventsInThisDayUserOwner) {
            if (!allEvents.contains(event)) {
                allEvents.add(event);
            }
        }
        for (Events event : eventsInThisDayUserInvitated) {
            if (!allEvents.contains(event)) {
                allEvents.add(event);
            }
        }
        for (Events event : eventsInThisDayGroupsMember) {
            if (!allEvents.contains(event)) {
                allEvents.add(event);
            }
        }
        for (Events event : birthdayEvents) {
            if (!allEvents.contains(event)) {
                allEvents.add(event);
            }
        }
        return allEvents;
    }

    @Override
    public List<Events> generateAllEvents(Long id, int month, int year) {
        List<Events> allEvents = new ArrayList<>();

        // Ottenere il numero di giorni nel mese
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Ciclare attraverso tutti i giorni del mese
        for (int day = 1; day <= daysInMonth; day++) {
            // Ottenere gli eventi per il giorno corrente
            List<Events> events = generateEvents(id, day, month, year);

            // Aggiungere gli eventi alla lista complessiva
            for (Events event : events) {
                if (!allEvents.contains(event)) {
                    allEvents.add(event);
                }
            }
        }

        return allEvents;
    }

    @Override
    public String modifyEvent(EventRequest eventRequest, Long id, String url) {
        Events event = eventsRepository.findById(id).orElseThrow();
        // Name
        event.setName(eventRequest.getEventName());
        // Descrizione
        event.setDescription(eventRequest.getEventDescription());
        // Location
        event.setLocation(eventRequest.getEventLocation());
        // Link
        event.setLink(eventRequest.getEventLink());
        // Visibilità
        event.setVisibility(eventRequest.getEventVisibility());
        // Controllo data - non può essere prima di Oggi!
        if (!eventRequest.getEventDate().isBefore(LocalDate.now())) {
            // Data
            Calendar data = Calendar.getInstance();
            data.set(eventRequest.getEventDate().getYear(), eventRequest.getEventDate().getMonthValue() - 1, eventRequest.getEventDate().getDayOfMonth());
            event.setDate(data);
        } else {
            return "BEFORE_TODAY";
        }
        // All day
        event.setAllDay(eventRequest.isAllDayEvent());
        // Se non è All day - set Inizio ora e Fine ora
        if (!eventRequest.isAllDayEvent()) {
            //  Controllo che ora inizio sia prima di ora di fine
            //  Se l'ora di fine è prima dell'ora di inizio
            if (eventRequest.getEventEndTime().isBefore(eventRequest.getEventStartTime())) {
                return "ERR_TIME";
            } else if (eventRequest.getEventStartTime().equals(eventRequest.getEventEndTime())) {
                //  Se l'ora di inizio è uguale a quella di fine
                return "ERR_TIME";
            } else {
                event.setBeginHour(eventRequest.getEventStartTime());
                event.setEndHour(eventRequest.getEventEndTime());
            }
        } else {
            event.setBeginHour(null);
            event.setEndHour(null);
        }
        // All User - In questo modo se un utente nuovo viene creato, non serve che sia inserito tra gli invitati dopo
        event.setAllUserInvitated(eventRequest.isAllUsers());
        //   Se tutti gli utenti sono invitati - non segno gli invitati
        if (!eventRequest.isAllUsers()) {
            List<Long> idUserInvitated = eventRequest.getUserIds();
            List<Users> userInvitated = new ArrayList<>();
            for (Long userId : idUserInvitated) {
                Users user = usersRepository.findById(userId).orElseThrow();
                userInvitated.add(user);
            }
            event.setInvitedUsers(userInvitated);
        } else {
            event.setInvitedUsers(new ArrayList<>());
        }

        // Gruppi invitati
        List<Long> idGroupInvitated = eventRequest.getGroupIds();
        List<UserGroups> groupInvitated = new ArrayList<>();
        for (Long groupId : idGroupInvitated) {
            UserGroups group = groupsRepository.findById(groupId).orElseThrow();
            groupInvitated.add(group);
        }
        event.setInvitedGroups(groupInvitated);
        event.setFestivity(false);
        Events eventModified = eventsRepository.save(event);

        return "OK";
    }

    //  TODO: Mandare email di modifica evento
}
