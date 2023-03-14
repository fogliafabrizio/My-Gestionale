package it.fogliafabrizio.mygestionale.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Aggiungi qui la logica per la gestione dell'errore e la definizione della pagina di errore
        model.addAttribute("errorMessageTitle", "404 Not Found");
        model.addAttribute("errorMessageSubtitle", "La pagina non esiste o c'è stato un problema interno del Server. Per favore scrivici se stai riscontrando dei problemi.");
        return "error"; // Restituisci il nome della view di errore personalizzata
    }

}
