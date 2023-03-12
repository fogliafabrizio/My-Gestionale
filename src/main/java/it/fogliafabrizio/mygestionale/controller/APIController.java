package it.fogliafabrizio.mygestionale.controller;

import it.fogliafabrizio.mygestionale.model.EventRequest;
import it.fogliafabrizio.mygestionale.model.Events;
import it.fogliafabrizio.mygestionale.model.UserGroups;
import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.repository.EventsRepository;
import it.fogliafabrizio.mygestionale.repository.UserGroupsRepository;
import it.fogliafabrizio.mygestionale.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class APIController {

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

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserGroupsRepository groupsRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/user/personal_data/{id}")
    public Users getPersonalData(
            @PathVariable("id") Long id
    ){
        Users user = usersRepository.findById(id).orElseThrow();
        user.setPassword("");
        return user;
    }

    @PostMapping("/user/change_password/{id}")
    public String changePassword(
            @PathVariable("id") Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword
    ){
        //  Primo controllo che la vecchia password sia corretta
        Users user = usersRepository.findById(id).orElseThrow();
        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            // Se la password vecchia non è corretta - ERRORE
            return "OLD_PSW_NOT_OK";
        } else {
            //  La password vecchia è corretta
            //  Secondo controllo : Le due password nuove corrispondono?
            if(!newPassword.equals(confirmPassword)){
                //  Le due password non corrispondono
                return "NOT_MATCH_PSW";
            } else {
                // Terzo controllo : La password deve rispettare la regola REGEX
                if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")){
                    //  Non rispetta la regola REGEX
                    return "NOT_REGEX_PSW";
                } else {
                    //  OK - Superato tutti i controlli => da salvare all'utente.
                    String newPasswordEncoded = passwordEncoder.encode(newPassword);
                    user.setPassword(newPasswordEncoded);
                    usersRepository.save(user);
                    return "OK PASSWORD";
                }
            }
        }
    }

    @PostMapping("/user/personal_data/birthdate/{id}")
    public String editDob(
            @PathVariable("id") Long id,
            @RequestParam String birthdate
    ) throws ParseException {
        Users user = usersRepository.findById(id).orElseThrow();
        System.out.println("Data: " + birthdate);
        if (birthdate.equals("")){
            user.setDateOfBirthday(null);
            usersRepository.save(user);
            return "BIRTH_RESET";
        } else if (birthdate.equals("Non definita")) {
            return "BIRTH_NOT_OK";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(birthdate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            user.setDateOfBirthday(calendar);

            usersRepository.save(user);

            System.out.println(user.getDateOfBirthday());

            return "BIRTH_OK";
        } catch (ParseException e){
            return "BIRTH_NOT_OK";
        }

    }

    @PostMapping("/calendar/getEvents")
    public List<Events> getEvents(
            @RequestParam int day,
            @RequestParam int month,
            @RequestParam int year
    ){
        List<Events> events = new ArrayList<>();
        Calendar data = Calendar.getInstance();
        day = 25;
        month = 12;
        year = 2024;
        data.set(Calendar.DAY_OF_MONTH, day);
        switch (month){
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
        System.out.println(data);
        /*
            -   User - EVENTS
            -   Events - (DATA)
                -   Festività
                -   AllUserInvitated
         */
        events = eventsRepository.findByDate(data);
        return events;
    }

    @PostMapping("/calendar/createEvent/{id}")
    public String createEvent(
            @RequestBody EventRequest eventRequest,
            @PathVariable Long id
    ) {
        Events event = new Events();
        event.setName(eventRequest.getEventName());
        event.setUserCreator(usersRepository.findById(id).orElseThrow());
        event.setFestivity(false);
        System.out.println(eventRequest.getEventDate());

        return "OK";
    }
}
