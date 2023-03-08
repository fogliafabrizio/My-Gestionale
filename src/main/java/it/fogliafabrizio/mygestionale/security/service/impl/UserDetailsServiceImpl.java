package it.fogliafabrizio.mygestionale.security.service.impl;

import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.model.security.CustomUserDetails;
import it.fogliafabrizio.mygestionale.resporitory.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(email);
        if(user != null){
            return new CustomUserDetails(user);
        } else {
            throw new UsernameNotFoundException("Username not found!");
        }
    }
}
