package com.example.devottask.api.v1.request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.constraints.NotEmpty;

// import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AccountRegistrationRequest {

  @NotEmpty private String name;

  @NotEmpty private String password;

  @NotEmpty private String email;

  private static final String EMAIL_REGEX =
      "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

      public AccountRegistrationRequest(String name, String password, String email) {
        // if (name == null || name.trim().isEmpty()) {
        //     throw new IllegalArgumentException("Name cannot be empty");
        // }
        // if (password == null || password.trim().isEmpty()) {
        //     throw new IllegalArgumentException("Password cannot be empty");
        // }
        // if (email == null || email.trim().isEmpty()) {
        //     throw new IllegalArgumentException("Email cannot be empty");
        // }
        // if (!isValidEmail(email)) {
        //     throw new IllegalArgumentException("Invalid email format");
        // }
    
        this.name = name;
        this.password = password;
        this.email = email;

    // Check if email is valid
    if (!isValidEmail(getEmail())) {
      throw new IllegalArgumentException("Invalid email format");
    }
  }

  private boolean isValidEmail(String email) {
    Pattern pattern = Pattern.compile(EMAIL_REGEX);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  
}
