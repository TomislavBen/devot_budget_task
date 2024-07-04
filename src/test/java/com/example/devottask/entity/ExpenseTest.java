package com.example.devottask.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {

  private Expense expense;
  private Account account;
  private Category category;

  @BeforeEach
  void setUp() {
    expense = new Expense();
    account = new Account();
    category = new Category();

    account.setId(1L);
    category.setId(1L);

    expense.setAccount(account).setCategory(category);
  }

  @Test
  void testExpenseCreation() {
    expense.setId(1L).setCost(100L).setDescription("Coffee").setTransactionDate(LocalDate.of(2023, 4, 1));
    assertNotNull(expense);
    assertEquals(1L, expense.getId());
    assertEquals(100L, expense.getCost());
    assertEquals("Coffee", expense.getDescription());
    assertEquals(LocalDate.of(2023, 4, 1), expense.getTransactionDate());
    assertEquals(account, expense.getAccount());
    assertEquals(category, expense.getCategory());
  }

  @Test
  void testPrePersist() {
    expense.prePersist();
    assertNotNull(expense.getTransactionDate());
    assertEquals(LocalDate.now(), expense.getTransactionDate());
  }
}
