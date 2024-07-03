package com.example.devottask.repository;

import java.util.List;
import java.util.Optional;

import com.example.devottask.entity.Account;

public interface AccountRepo {

  String checkIfMailInUse(String email);

  Optional<String> checkIfMailInUseOptional(String email);

  boolean doesAccountExistByEmail(String email);

  Optional<Account> findByEmail(String email);

  Account save(Account account);

  List<Account> findAll();

  Account findById(Long id);
}
