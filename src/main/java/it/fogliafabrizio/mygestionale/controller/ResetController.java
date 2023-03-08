package it.fogliafabrizio.mygestionale.controller;

import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reset")
public class ResetController {

    @Autowired
    UserService userService;

    @GetMapping
    public String reset(
            @Param("code") String code,
            Model model,
            @RequestParam(name = "redirect", required = false) boolean redirect,
            HttpSession session
    ){
        if(redirect == false){
            session.removeAttribute("msgErrorResetPsw");
        }
        Users userToVerify = userService.verifyCode(code);
        if (userToVerify != null){
            model.addAttribute("title", "Reset Password");
            model.addAttribute("code", code);
            return "resetPassword_success";
        } else {
            model.addAttribute("title", "Reset Password");
            return "resetPassword_error";
        }
    }

    @PostMapping("/confirm")
    public String resetPassword(
            @RequestParam String code,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ){
        redirectAttributes.addAttribute("redirect", true);
        session.removeAttribute("msgErrorResetPsw");
        session.removeAttribute("msgSuccessResetPsw");
        //  Controllo che le password siano uguali
        if(!password.equals(confirmPassword)){
            session.setAttribute("msgErrorResetPsw", "Le due password non corrispondono!");
        } else {
            if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
                session.setAttribute("msgErrorResetPsw", "La password non rispetta le regole!");
            } else {
                Users user_enabled = userService.enabledUsers(password, code);
                if (user_enabled != null) {
                    //  L'utente sarà abilitato
                    session.setAttribute("msgSuccessResetPsw", "Password reimpostata!");
                    return "redirect:/signin";
                } else {
                    session.setAttribute("msgErrorResetPsw", "Internal Server Error");
                }
            }
        }
        return "redirect:/reset?code=" + code;
    }
}
