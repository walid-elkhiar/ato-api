package com.ato.backendapi.security.service;

import com.ato.backendapi.entities.Utilisateurs;
import com.ato.backendapi.repositories.UtilisateursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OurUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateursRepository utilisateursRepository;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
////        return utilisateursRepository.findByAdresseMail(username).orElseThrow();
//        return utilisateursRepository.findByAdresseMail(username);
//
//    }
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Utilisateurs user = utilisateursRepository.findByAdresseMail(mail);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new OurUserDetails(user);
    }
}
