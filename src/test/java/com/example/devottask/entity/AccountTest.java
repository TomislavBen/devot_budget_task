package com.example.devottask.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setName("Test User").setEmail("test@example.com").setPassword("password").setBalance(1000L);
    }

    @Test
    void setPasswordAndCheckPassword() {
        String newPassword = "newPassword";
        account.setPassword(newPassword);
        assertTrue(account.checkPassword(newPassword));
    }

    @Test
    void setBalanceWithValidValue() {
        account.setBalance(500L);
        assertEquals(500L, account.getBalance());
    }

    @Test
    void setBalanceWithInvalidValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> account.setBalance(-100L));
        assertEquals("Balance cannot be less than 0", exception.getMessage());
    }

    @Test
    void reduceBalanceWithSufficientFunds() {
        account.reduceBalance(500L);
        assertEquals(500L, account.getBalance());
    }

    @Test
    void reduceBalanceWithInsufficientFunds() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> account.reduceBalance(1500L));
        assertEquals("Insufficient balance.", exception.getMessage());
    }
}
