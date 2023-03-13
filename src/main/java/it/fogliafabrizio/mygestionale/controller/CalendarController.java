package it.fogliafabrizio.mygestionale.controller;

import it.fogliafabrizio.mygestionale.model.Events;
import it.fogliafabrizio.mygestionale.model.UserGroups;
import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.model.Visibility;
import it.fogliafabrizio.mygestionale.repository.EventsRepository;
import it.fogliafabrizio.mygestionale.repository.UserGroupsRepository;
import it.fogliafabrizio.mygestionale.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserGroupsRepository groupsRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @ModelAttribute
    private void userDetails(
            Model model,
            Principal principal
    ){
        if(principal != null){
            String email = principal.getName();
            Users user = usersRepository.findByEmail(email);
            model.addAttribute("user", user);
        }
    }

    @GetMapping
    public String homeGroups(
            Model model,
            Principal principal
    ){
        model.addAttribute("title", "Calendario");
        model.addAttribute("cssFile", "/css/calendar.css");
        model.addAttribute("jsFile", "/js/calendar.js");

        /* Lista di Utenti tranne chi ha fatto l'accesso */
        List<Users> allUsers = usersRepository.findAll();
        String email = principal.getName();
        Users user = usersRepository.findByEmail(email);
        allUsers.remove(user);
        System.out.println(allUsers);
        model.addAttribute("usersList", allUsers);

        /* Lista di Gruppi pubblici o utente è admin */
        List<UserGroups> allGroups = groupsRepository.findPublicOrAdminGroups(user);
        model.addAttribute("groupsList", allGroups);

        return "calendar";
    }

    @GetMapping("/event/{id}")
    public String getEventInfo(
            @PathVariable Long id,
            Model model,
            Principal principal
    ){
        try {
            Events event = eventsRepository.findById(id).orElseThrow();
            // Resto del codice se l'evento esiste
            // Controlla se l'utente è proprietario dell'evento o se l'evento è pubblico o se l'utente è stato invitato o è membro del gruppo degli utenti
            boolean authorized = false;
            if (principal != null) {
                String email = principal.getName();
                Users user = usersRepository.findByEmail(email);
                if(event.isFestivity()){
                    model.addAttribute("title", "Errore Evento");
                    model.addAttribute("errorMessageTitle", "Evento non visualizzabile");
                    model.addAttribute("errorMessageSubtitle", "L'evento che stai cercando non è visualizzabile");
                    return "error";
                } else if (event.getUserCreator().equals(user) || event.getVisibility() == Visibility.PUBLIC
                        || event.getInvitedUsers().contains(user) || user.getGroups().contains(event.getInvitedGroups())) {
                    authorized = true;
                }
            }
            if(authorized) {
                /* Lista di Utenti tranne chi ha fatto l'accesso */
                List<Users> allUsers = usersRepository.findAll();
                String email = principal.getName();
                Users user = usersRepository.findByEmail(email);
                allUsers.remove(user);

                model.addAttribute("usersList", allUsers);
                //  TODO: possibile che devo ripetere tutto anche qui?
                /* Lista di Gruppi pubblici o utente è admin */
                List<UserGroups> allGroups = groupsRepository.findPublicOrAdminGroups(user);
                model.addAttribute("groupsList", allGroups);


                model.addAttribute("title", event.getName());
                model.addAttribute("event", event);
                model.addAttribute("jsFile", "/js/event_info.js");
                return "event_info";
            } else {
                model.addAttribute("title", "Accesso Negato");
                model.addAttribute("errorMessageTitle", "Non hai il permesso per accedere a questo evento.");
                model.addAttribute("errorMessageSubtitle", "Per vedere questo evento privato devi essere invitato o fare parte di uno dei gruppi invitati. Se pensi che ci sia un errore, scrivici.");
                return "error";
            }
        } catch (NoSuchElementException ex) {
            model.addAttribute("title", "Evento non trovato");
            // Imposta il messaggio di errore personalizzato
            model.addAttribute("errorMessageTitle", "Evento non trovato");
            model.addAttribute("errorMessageSubtitle", "L'evento che stai cercando non esiste");
            return "error";
        }
    }
}
