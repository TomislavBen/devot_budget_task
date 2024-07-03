package com.example.devottask.api.v1.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.devottask.api.v1.request.AccountRegistrationRequest;
import com.example.devottask.entity.Account;
import com.example.devottask.enums.PredefinedCategories;
import com.example.devottask.repository.AccountRepo;
import com.example.devottask.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequestMapping(path = "/api/v1")
@RestController
@RequiredArgsConstructor
public class AccountController {

  private final AccountRepo accountRepo;
  private final AccountService accountService;
  private final PredefinedCategories predefinedCategories;

  @PostMapping("/register")
  public ResponseEntity<?> registerAccount(@Valid @RequestBody AccountRegistrationRequest registrationRequest)
      throws Exception {
    Account acc = new Account()
        .setName(registrationRequest.getName())
        .setEmail(accountRepo.checkIfMailInUse(registrationRequest.getEmail()))
        .setPassword(registrationRequest.getPassword());

    accountRepo.save(acc);

    predefinedCategories.createPredefinedCategoriesForAccount(acc);

    return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "Account registered"));
  }

  @PatchMapping("/accounts/update")
  public ResponseEntity<?> updateAccountBalance(@RequestBody Long deposit)
      throws Exception {
    Account account = accountService.checkAccount();

    return ResponseEntity.ok(accountRepo.save(account.setBalance(account.getBalance() + deposit)));
  }
}
