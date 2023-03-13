package it.fogliafabrizio.mygestionale.controller;

import it.fogliafabrizio.mygestionale.model.EventRequest;
import it.fogliafabrizio.mygestionale.model.Events;
import it.fogliafabrizio.mygestionale.model.UserGroups;
import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.repository.EventsRepository;
import it.fogliafabrizio.mygestionale.repository.UserGroupsRepository;
import it.fogliafabrizio.mygestionale.repository.UsersRepository;
import it.fogliafabrizio.mygestionale.service.EventService;
import it.fogliafabrizio.mygestionale.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jdk.jfr.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    private UserServiceImpl userService;

    @Autowired
    private UserGroupsRepository groupsRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private EventService eventService;

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
        return userService.changePassword(id, oldPassword, newPassword, confirmPassword);
    }

    @PostMapping("/user/personal_data/birthdate/{id}")
    public String editDob(
            @PathVariable("id") Long id,
            @RequestParam String birthdate
    ) throws ParseException {
        Users user = usersRepository.findById(id).orElseThrow();
        return userService.changeBirthdate(user, birthdate);
    }

    @PostMapping("/calendar/getEvents/{id}")
    public List<Events> getEvents(
            @PathVariable("id") Long id,
            @RequestParam int day,
            @RequestParam int month,
            @RequestParam int year
    ){
        List<Events> events = eventService.generateEvents(id, day, month, year);
        //System.out.println(events);
        return events;
    }

    @PostMapping("/calendar/getAllEvents/{id}")
    public List<Events> getAllEvents(
            @PathVariable("id") Long id,
            @RequestParam int month,
            @RequestParam int year
    ){
        return eventService.generateAllEvents(id, month, year);
    }

    @PostMapping("/calendar/createEvent/{id}")
    public String createEvent(
            @RequestBody EventRequest eventRequest,
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        String url = request.getRequestURL().toString().replace(request.getServletPath(), "");
        return eventService.createEvent(eventRequest, id, url);
    }

    @PostMapping("/calendar/modifyEvent/{id}")
    public String modifyEvent(
            @RequestBody EventRequest eventRequest,
            @PathVariable Long id
            ){
        return eventService.modifyEvent(eventRequest,id);
    }

    @PostMapping("/calendar/deleteEvent/{id}")
    public void deleteEvent(
            @PathVariable Long id
    ){
        Events events = eventsRepository.findById(id).orElseThrow();
        eventsRepository.delete(events);
    }
}
