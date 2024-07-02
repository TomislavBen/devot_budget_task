package com.example.devottask.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.devottask.entity.Account;
import com.example.devottask.repository.AccountRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepo accountRepo;


  public Account checkAccount() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (principal instanceof UserDetails) {
      String accountEmail = ((UserDetails) principal).getUsername();

      return accountRepo
          .findByEmail(accountEmail)
          .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    throw new IllegalArgumentException("Not logged in");
  }

}
