package it.fogliafabrizio.mygestionale.service.impl;

import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.repository.UsersRepository;
import it.fogliafabrizio.mygestionale.service.UserService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public boolean checkEmail(String email) {
        boolean existMail = usersRepository.existsByEmail(email);
        return existMail;
    }

    @Override
    public Users createUser(Users user, String url) {
        //  Viene settata una password temporanea
        String randomPassword = UUID.randomUUID().toString().substring(0, 12);
        System.out.println("Code_creation" + randomPassword);
        user.setPassword(passwordEncoder.encode(randomPassword));
        System.out.println("Code_crypt:" + user.getPassword());
        //  Viene disattivato l'account finchè l'utente non cambia la password
        user.setEnabled(false);

        //  Viene impostata la email con @rationence.eu
        user.setEmail(user.getEmail() + "@rationence.eu");

        //  Viene salvato l'utente
        Users user_saved = usersRepository.save(user);

        //  Da inviare l'utente la pagina di cambio password e attivazione
        sendVerificationEmail(user_saved, url, user_saved.getPassword());

        return user_saved;
    }

    @Override
    public Users verifyCode(String code) {
        Users user_new = usersRepository.findByPassword(code);
        //System.out.println(user_new.getPassword());
        return user_new;
    }

    @Override
    public Users enabledUsers(String password, String code) {
        Users userToEnabled = usersRepository.findByPassword(code);
        userToEnabled.setPassword(passwordEncoder.encode(password));
        userToEnabled.setEnabled(true);
        usersRepository.save(userToEnabled);
        return userToEnabled;
    }

    @Override
    public Users resetPassword(Users user, String url) {
        user.setEnabled(false);
        String randomPassword = UUID.randomUUID().toString().substring(0, 12);
        user.setPassword(passwordEncoder.encode(randomPassword));
        //  Viene salvato l'utente
        Users user_saved = usersRepository.save(user);

        //  Da inviare l'utente la pagina di cambio password e attivazione
        sendForgotEmail(user_saved, url, user_saved.getPassword());

        return user_saved;

    }

    @Override
    public String changePassword(Long id, String oldPassword, String newPassword, String confirmPassword) {
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

    @Override
    public String changeBirthdate(Users user, String birthdate) {
        if (birthdate.equals("")){
            user.setDateOfBirthday(null);
            usersRepository.save(user);
            return "BIRTH_RESET";
        } else if (birthdate.equals("Non definita")) {
            return "BIRTH_NOT_OK";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(birthdate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            user.setDateOfBirthday(calendar);

            usersRepository.save(user);

            System.out.println(user.getDateOfBirthday());

            return "BIRTH_OK";
        } catch (ParseException e){
            return "BIRTH_NOT_OK";
        }
    }

    private void sendForgotEmail(Users userSaved, String url, String randomPassword) {
        String from = "info@rationence.eu";
        String to = userSaved.getEmail();
        String subject = "Password Reset";
        String content = "Ciao [[name]]! <br> Per poter reimpostare la password clicca sul link qua sotto: <br> <h3><a href=\"[[URL]]\" target=\"_self\"> CLICCA QUI </a></h3> Se non dovesse visualizzarsi, copia questo link <a href=\"[[URL]]\" target=\"_self\"> [[URL]] </a><br> Grazie, <br> Rationence.";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);

            content=content.replace("[[name]]", userSaved.getFirstName());

            String siteUrl = url+"/reset?code="+randomPassword;
            content=content.replace("[[URL]]", siteUrl);

            helper.setText(content, true);

            javaMailSender.send(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendVerificationEmail(Users user, String url, String randomPassword) {
        //System.out.println("ENTRO");
        String from = "info@rationence.eu";
        String to = user.getEmail();
        String subject = "Account Verification";
        String content = "Welcome on board [[name]]! <br> Per poter accedere al sito del PAT ti prego di cliccare sul link e cambiare la tua password: <br> <h3><a href=\"[[URL]]\" target=\"_self\"> CLICCA QUI </a></h3> Se non dovesse visualizzarsi, copia questo link <a href=\"[[URL]]\" target=\"_self\"> [[URL]] </a><br> Grazie, <br> Rationence.";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);

            content=content.replace("[[name]]", user.getFirstName());

            String siteUrl = url+"/verify?code="+randomPassword;
            content=content.replace("[[URL]]", siteUrl);

            helper.setText(content, true);

            javaMailSender.send(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
