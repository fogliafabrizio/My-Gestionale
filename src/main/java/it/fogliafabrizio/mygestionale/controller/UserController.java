package it.fogliafabrizio.mygestionale.controller;

import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.resporitory.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

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

    @GetMapping("/edit")
    public String editUser(
            Model model
    ){
        model.addAttribute("title", "Edit profile");
        return "edit_profile";
    }
}
