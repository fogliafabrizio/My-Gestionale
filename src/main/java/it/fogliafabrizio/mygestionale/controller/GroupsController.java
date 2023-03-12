package it.fogliafabrizio.mygestionale.controller;

import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    @Autowired
    private UsersRepository usersRepository;

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
    public String homeGroups(){
        return "groups";
    }
}
