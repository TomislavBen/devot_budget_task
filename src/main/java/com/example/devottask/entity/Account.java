package com.example.devottask.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "account")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String name;

  @NotNull
  @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
  @Column(unique = true)
  private String email;

  @JsonIgnore
  @NotNull
  private String password;

  private Long balance = 1000L;

  private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public String getPassword() {
    return password;
  }

  public Account setPassword(String password) {
    this.password = this.hashPassword(password);
    return this;
  }

  private String hashPassword(String password) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    return passwordEncoder.encode(password);
  }

  public boolean checkPassword(String password) {
    return encoder.matches(password, this.getPassword());
  }

  public Account setBalance(Long balance) {
    if (balance < 0) {
      throw new IllegalArgumentException("Balance cannot be less than 0");
    }
    this.balance = balance;
    return this;
  }

  public void reduceBalance(Long cost) {
    if (balance >= cost) {
      balance -= cost;
    } else {
      throw new IllegalArgumentException("Insufficient balance.");
    }
  }

  public void adjustBalanceForExpenseUpdate(Long originalCost, Long newCost) {
    Long costDifference = newCost - originalCost;
    reduceBalance(costDifference);
  }
}
