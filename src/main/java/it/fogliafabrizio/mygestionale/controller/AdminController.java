package it.fogliafabrizio.mygestionale.controller;

import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.resporitory.UsersRepository;
import it.fogliafabrizio.mygestionale.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserService userService;

    @ModelAttribute
    private void userDetails(
            Model model,
            Principal principal
    ){
        String email = principal.getName();
        Users user = usersRepository.findByEmail(email);
        model.addAttribute("user", user);
    }

    @GetMapping("/newUser")
    public String newUser(
            Model model,
            @RequestParam(name = "redirect", required = false) boolean redirect,
            HttpSession session
    ){
        if(redirect == false){
            session.removeAttribute("msgErrorRegistration");
            session.removeAttribute("msgSuccessRegistration");
        }
        model.addAttribute("title", "Nuovo Utente");
        return "newUser";
    }

    @PostMapping("/newUser")
    public String newUser(
            @ModelAttribute Users user,
            HttpSession session,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ){
        redirectAttributes.addAttribute("redirect", true);
        session.removeAttribute("msgErrorRegistration");
        session.removeAttribute("msgSuccessRegistration");
        //  Prima controllo che email non esiste già!
        String email = user.getEmail() + "@rationence.eu";
        boolean exist = userService.checkEmail(email);
        System.out.println(exist);
        if (exist) {
            //  Se esiste la mail torna alla pagina di registrazione
            session.setAttribute("msgErrorRegistration", "Email già esistente!");
        } else {
            //  Se non esiste la mail bisogna creare l'utente e mandare l'email

            //  Prendiamo l'url del sito
            String url = request.getRequestURL().toString().replace(request.getServletPath(), "");

            //System.out.println(url);
            //  Creiamo utente e inviamo Mail
            Users user_new = userService.createUser(user, url);

            if(user_new != null){
                session.setAttribute("msgSuccessRegistration", "Registrazione avvenuta con successo!");
            } else {
                session.setAttribute("msgErrorRegistration", "Server error");
            }
        }
        //  Se non esiste - Creo utente e mando email all'utente
        return "redirect:/admin/newUser";
    }
}
