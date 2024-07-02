package com.example.devottask.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;


import com.example.devottask.entity.Account;
import com.example.devottask.repository.AccountRepo;

import java.util.Collections;

@Service
public class AccountDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepo accountRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account acc = accountRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new User(acc.getEmail(), acc.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }
}
