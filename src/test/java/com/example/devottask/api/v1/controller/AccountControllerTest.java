package com.example.devottask.api.v1.controller;

import com.example.devottask.api.v1.request.AccountRegistrationRequest;
import com.example.devottask.entity.Account;
import com.example.devottask.enums.PredefinedCategories;
import com.example.devottask.repository.AccountRepo;
import com.example.devottask.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AccountRepo accountRepo;

  @MockBean
  private AccountService accountService;

  @MockBean
  private PredefinedCategories predefinedCategories;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testRegisterAccount() throws Exception {
    AccountRegistrationRequest registrationRequest = new AccountRegistrationRequest("John Doe", "password123",
        "john@example.com");
    String jsonRequest = objectMapper.writeValueAsString(registrationRequest);

    when(accountRepo.checkIfMailInUse("john@example.com")).thenReturn("john@example.com");

    mockMvc.perform(post("/api/v1/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isCreated())
        .andExpect(content().json("{'message':'Account registered'}"));

    ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
    verify(accountRepo).save(accountCaptor.capture());
    Account savedAccount = accountCaptor.getValue();

    assertEquals("John Doe", savedAccount.getName());
    assertEquals("john@example.com", savedAccount.getEmail());

    verify(predefinedCategories).createPredefinedCategoriesForAccount(savedAccount);
  }

  @Test
  void testUpdateAccountBalance() throws Exception {
    Long deposit = 100L;
    Account account = new Account().setId(1L).setBalance(200L);
    given(accountService.checkAccount()).willReturn(account);

    mockMvc.perform(patch("/api/v1/accounts/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(deposit)))
        .andExpect(status().isOk());

    verify(accountRepo).save(account.setBalance(300L));
  }
}
