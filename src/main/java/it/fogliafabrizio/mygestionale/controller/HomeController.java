package it.fogliafabrizio.mygestionale.controller;

import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.repository.UsersRepository;
import it.fogliafabrizio.mygestionale.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;


@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    UserService userService;

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

    @GetMapping("/signin")
    public String home(
            Model model,
            @RequestParam(name = "redirect", required = false) boolean redirect,
            HttpSession session
    ){
        if(redirect == false){
            session.removeAttribute("msgSuccessForgot");
            session.removeAttribute("msgSuccessResetPsw");
            session.removeAttribute("msgSuccessVerify");
        }
        model.addAttribute("title", "Login Page");
        model.addAttribute("cssFile", "css/login.css");
        return "login";
    }

    @GetMapping
    public String index(
            Model model
    ){
        model.addAttribute("title", "Home Page");
        return "index";
    }

    @GetMapping("/forgot")
    public String forgotPassword(
            Model model,
            @RequestParam(name = "redirect", required = false) boolean redirect,
            HttpSession session
    ){
        if(redirect == false){
            session.removeAttribute("msgErrorForgot");
        }
        model.addAttribute("title", "Hai dimenticato la password?");
        return "forgotPassword";
    }

    @PostMapping("/forgot")
    public String setForgotPassword(
            @RequestParam String email,
            HttpSession session,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ){
        redirectAttributes.addAttribute("redirect", true);
        session.removeAttribute("msgErrorForgot");
        session.removeAttribute("msgSuccessForgot");
        boolean exist = userService.checkEmail(email);
        if (!exist) {
            //  Se esiste la mail torna alla pagina di registrazione
            session.setAttribute("msgErrorForgot", "Questo indirizzo non è registrato o è stato scritto male!");
            return "redirect:/forgot";
        } else {
            Users user = usersRepository.findByEmail(email);
            if (!user.isEnabled()){
                session.setAttribute("msgErrorForgot", "Questo indirizzo non è abilitato! Se hai bisogno di aiuto, scrivici per maggiori informazioni.");
                return "redirect:/forgot";
            }
            String url = request.getRequestURL().toString().replace(request.getServletPath(), "");
            Users user_reset = userService.resetPassword(user, url);
            if(user_reset != null){
                session.setAttribute("msgSuccessForgot", "Reset della password avvenuto con successo. Controlla la tua casella di posta");
                return "redirect:/signin";
            } else {
                session.setAttribute("msgErrorForgot", "Server error");
                return "redirect:/forgot";
            }
        }
    }

}
