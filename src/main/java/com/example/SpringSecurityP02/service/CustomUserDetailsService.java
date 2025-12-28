package com.example.SpringSecurityP02.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        /*
            if (!username.equals("paras")){
                 throw new UsernameNotFoundException("User Not found");
             }
             String encodedPass= new BCryptPasswordEncoder().encode("root");
             return User.withUsername("paras").password("root").roles("USER ").build();
        */

        String encodedPass1= new BCryptPasswordEncoder().encode("root");
        if(username.equals("paras")){
            return User.withUsername("paras").password(encodedPass1).roles("ADMIN").build();
        }

        String encodedPass2= new BCryptPasswordEncoder().encode("root");

        if(username.equals("prachi")){
            return User.withUsername("prachi").password(encodedPass2).roles("USER").build();
        }

        throw new UsernameNotFoundException("User not found");
    }
}
