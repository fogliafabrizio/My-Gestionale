package it.fogliafabrizio.mygestionale.service;

import it.fogliafabrizio.mygestionale.model.Users;
import org.springframework.stereotype.Service;


public interface UserService {

    public boolean checkEmail(String email);

    public Users createUser(Users user, String url);

    public Users verifyCode(String code);

    public Users enabledUsers(String password, String code);

    public Users resetPassword(Users user, String url);

    public String changePassword(Long id, String oldPassword, String newPassword, String confirmPassword);

    public String changeBirthdate(Users user, String birthdate);
}
