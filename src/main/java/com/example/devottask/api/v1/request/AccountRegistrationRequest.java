package com.example.devottask.api.v1.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AccountRegistrationRequest {

  @NotEmpty
  private String name;

  @NotEmpty
  private String password;

  @NotEmpty
  @Email(message = "Invalid format")
  private String email;

  public AccountRegistrationRequest(String name, String password, String email) {
    this.name = name;
    this.password = password;
    this.email = email;
  }
}
