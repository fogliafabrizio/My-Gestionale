package it.fogliafabrizio.mygestionale.controller;

import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/user/personal_data/{id}")
    public Users getPersonalData(
            @PathVariable("id") Long id
    ){
        System.out.println(id);
        Users user = usersRepository.findById(id).orElseThrow();
        user.setPassword("");
        return user;
    }

    @PostMapping("/user/change_password/{id}")
    public String changePassword(
            @PathVariable("id") Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword
    ){
        //  Primo controllo che la vecchia password sia corretta
        Users user = usersRepository.findById(id).orElseThrow();
        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            // Se la password vecchia non è corretta - ERRORE
            return "OLD_PSW_NOT_OK";
        } else {
            //  La password vecchia è corretta
            //  Secondo controllo : Le due password nuove corrispondono?
            if(!newPassword.equals(confirmPassword)){
                //  Le due password non corrispondono
                return "NOT_MATCH_PSW";
            } else {
                // Terzo controllo : La password deve rispettare la regola REGEX
                if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")){
                    //  Non rispetta la regola REGEX
                    return "NOT_REGEX_PSW";
                } else {
                    //  OK - Superato tutti i controlli => da salvare all'utente.
                    String newPasswordEncoded = passwordEncoder.encode(newPassword);
                    user.setPassword(newPasswordEncoded);
                    usersRepository.save(user);
                    return "OK PASSWORD";
                }
            }
        }
    }
}
