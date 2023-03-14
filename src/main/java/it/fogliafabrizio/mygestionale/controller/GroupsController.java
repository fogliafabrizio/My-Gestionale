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
import org.springframework.web.bind.annotation.RequestParam;

import javax.management.loading.PrivateClassLoader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupsController {

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
        model.addAttribute("title", "Gruppi");
        String email = principal.getName();
        Users user = usersRepository.findByEmail(email);
        List<UserGroups> groupsUser = groupsRepository.findByMemberUser(user);
        /* Lista di Utenti tranne chi ha fatto l'accesso */
        List<Users> allUsers = usersRepository.findAll();
        allUsers.remove(user);
        model.addAttribute("usersList", allUsers);
        model.addAttribute("groups", groupsUser);
        model.addAttribute("jsFile", "/js/groups.js");
        return "groups";
    }
}
