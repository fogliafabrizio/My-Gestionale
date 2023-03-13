package it.fogliafabrizio.mygestionale.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Aggiungi qui la logica per la gestione dell'errore e la definizione della pagina di errore
        return "error"; // Restituisci il nome della view di errore personalizzata
    }

}
