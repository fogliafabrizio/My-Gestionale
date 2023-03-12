package it.fogliafabrizio.mygestionale.controller;

import it.fogliafabrizio.mygestionale.model.UserGroups;
import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.repository.UserGroupsRepository;
import it.fogliafabrizio.mygestionale.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserGroupsRepository groupsRepository;

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
}
