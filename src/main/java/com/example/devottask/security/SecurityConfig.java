package com.example.devottask.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private static final String[] USER_SECURED_URLs = {
      "/api/v1/categories",
      "/api/v1/expenses",
      "/api/v1/accounts",
  };

  private static final String[] UN_SECURED_URLs = {
      "/api/v1/register",
      "/openapi.yaml"
  };

  @Bean
  public UserDetailsService userDetailsService() {
    return new AccountDetailsService();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());

    return authenticationProvider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors(cors -> {
    })
        .csrf(CsrfConfigurer::disable)
        .authorizeHttpRequests(
            authorize -> authorize
                .requestMatchers(UN_SECURED_URLs)
                .permitAll()
                .requestMatchers(USER_SECURED_URLs)
                .hasAuthority("USER")
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .anyRequest()
                .authenticated())
        .httpBasic(Customizer.withDefaults())
        .logout(
            logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll());

    return http.build();
  }
}
