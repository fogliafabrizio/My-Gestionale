package it.fogliafabrizio.mygestionale.controller;

import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/verify")
public class VerifyController {

    @Autowired
    UserService userService;

    @GetMapping
    public String verify(
            @Param("code") String code,
            Model model,
            @RequestParam(name = "redirect", required = false) boolean redirect,
            HttpSession session
    ){
        if(redirect == false){
            session.removeAttribute("msgErrorVerify");
           }
        Users userToVerify = userService.verifyCode(code);
        if (userToVerify != null){
            model.addAttribute("title", "Success Verification");
            model.addAttribute("code", code);
            return "verificationUser_success";
        } else {
            model.addAttribute("title", "Error Verification");
            model.addAttribute("errorMessageTitle", "Qualcosa è andato storto...");
            model.addAttribute("errorMessageSubtitle", "L'account è già abilitato o c'è stato un errore interno del server. Per favore contattaci se riscontri problemi.");
            return "error";
        }
    }

    @PostMapping("/confirm")
    public String confirmUser(
            @RequestParam String code,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ){
        redirectAttributes.addAttribute("redirect", true);
        session.removeAttribute("msgErrorVerify");
        session.removeAttribute("msgSuccessVerify");
        //  Controllo che le password siano uguali
        if(!password.equals(confirmPassword)){
            session.setAttribute("msgErrorVerify", "Le due password non corrispondono!");
        } else {
            if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
                /*
                    La password abbia almeno un numero e un carattere speciale (indicati tra parentesi quadre)
                    e che abbia almeno 8 caratteri totali. Inoltre, con il lookahead (?=\\S+$)
                    viene controllato che la password non contenga spazi bianchi all'interno.
                 */
                session.setAttribute("msgErrorVerify", "La password non rispetta le regole!");
            } else {
                Users user_enabled = userService.enabledUsers(password, code);
                if (user_enabled != null) {
                    //  L'utente sarà abilitato
                    session.setAttribute("msgSuccessVerify", "Abilitato con successo!");
                    return "redirect:/signin";
                } else {
                    session.setAttribute("msgErrorVerify", "Internal Server Error");
                }
            }
        }
        return "redirect:/verify?code=" + code;
    }
}
