package com.example.devottask.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.devottask.entity.Account;
import com.example.devottask.repository.AccountRepo;

@Configuration
public class SeedDatabase {

  @Bean
  CommandLineRunner initDatabase(AccountRepo accountRepo) {
    return args -> {
      seedAccount(accountRepo);
    };
  }

  private void seedAccount(AccountRepo accountRepository) {
    String email = "user@example.com";
    if (accountRepository.findByEmail(email).isEmpty()) {
      Account account = new Account()
          .setName("Seed User")
          .setEmail(email)
          .setPassword("password")
          .setBalance(1000L);

      accountRepository.save(account);
      System.out.println("Seeded account: " + account.getEmail());
    }
  }
}
