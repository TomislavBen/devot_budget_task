package com.example.devottask.api.v1.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.devottask.api.v1.request.CreateExpenseRequest;
import com.example.devottask.api.v1.request.ExpenseCostRequest;
import com.example.devottask.api.v1.request.ExpenseDateRequest;
import com.example.devottask.api.v1.request.UpdateExpenseRequest;
import com.example.devottask.entity.Account;
import com.example.devottask.entity.Category;
import com.example.devottask.entity.Expense;
import com.example.devottask.service.AccountService;
import com.example.devottask.service.CategoryService;
import com.example.devottask.repository.AccountRepo;
import com.example.devottask.repository.CategoryRepo;
import com.example.devottask.repository.ExpenseRepo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ExpensesControllerTest {

  @Mock
  private AccountService accountService;

  @Mock
  private CategoryService categoryService;

  @Mock
  private ExpenseRepo expenseRepo;

  @Mock
  private AccountRepo accountRepo;

  @Mock
  private CategoryRepo categoryRepo;

  @InjectMocks
  private ExpensesController expensesController;

  private Account mockAccount;
  private Category mockCategory;
  private List<Expense> mockExpenses;
  private ExpenseDateRequest testDateRequest;
  private ExpenseCostRequest testCostRequest;

  @Test
  public void testCreateExpense() throws Exception {
    CreateExpenseRequest request = new CreateExpenseRequest();
    request.setCategory("Food");
    request.setCost(10L);
    request.setDescription("Lunch");
    request.setTransactionDate(LocalDate.now());

    Account mockAccount = new Account().setBalance(10L);
    Category mockCategory = new Category().setName("Food");
    Expense expectedExpense = new Expense()
        .setCategory(mockCategory)
        .setCost(10L)
        .setDescription("Lunch")
        .setTransactionDate(LocalDate.now())
        .setAccount(mockAccount);

    when(accountService.checkAccount()).thenReturn(mockAccount);
    when(categoryService.getCategoryByNameForAccount(request.getCategory())).thenReturn(mockCategory);
    when(expenseRepo.save(any(Expense.class))).thenReturn(expectedExpense);

    ResponseEntity<Expense> response = expensesController.createExpense(request);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());
    Expense responseBody = response.getBody();
    assertNotNull(responseBody);
    assertEquals(expectedExpense.getDescription(), responseBody.getDescription());
    assertEquals(expectedExpense.getCost(), responseBody.getCost());

    verify(accountRepo).save(any(Account.class));
    verify(expenseRepo).save(any(Expense.class));
  }

  @Test
  void testGetExpenseById() {
    Long id = 1L;
    Expense mockExpense = new Expense();
    when(accountService.checkAccount()).thenReturn(new Account());
    when(expenseRepo.findByIdAndAccount(id, accountService.checkAccount())).thenReturn(mockExpense);

    ResponseEntity<Expense> response = expensesController.getExpenseById(id);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockExpense, response.getBody());
  }

  @Test
  void testGetAllExpenses() {
    List<Expense> mockExpenses = Arrays.asList(new Expense(), new Expense());
    when(accountService.checkAccount()).thenReturn(new Account());
    when(expenseRepo.findAllByAccount(accountService.checkAccount())).thenReturn(mockExpenses);

    ResponseEntity<List<Expense>> response = expensesController.getAllExpenses();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockExpenses, response.getBody());
  }

  @Test
  void testUpdateExpense() throws Exception {
    Long id = 1L;
    UpdateExpenseRequest updateExpenseRequest = new UpdateExpenseRequest();
    Expense mockExpense = new Expense();
    Account mockAccount = new Account();
    when(accountService.checkAccount()).thenReturn(mockAccount);
    when(expenseRepo.findByIdAndAccount(id, mockAccount)).thenReturn(mockExpense);
    when(categoryService.getCategoryByNameForAccountOptional(Optional.empty())).thenReturn(Optional.of(new Category()));

    ResponseEntity<Expense> response = expensesController.updateExpense(id, updateExpenseRequest);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(expenseRepo).save(any(Expense.class));
  }

  @Test
  void testDeleteExpense() {
    Long id = 1L;
    Expense mockExpense = new Expense();
    mockExpense.setCost(100L);
    Account mockAccount = new Account();
    mockAccount.setBalance(500L);
    when(accountService.checkAccount()).thenReturn(mockAccount);
    when(expenseRepo.findByIdAndAccount(id, mockAccount)).thenReturn(mockExpense);

    ResponseEntity<?> response = expensesController.deleteExpense(id);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(expenseRepo).delete(mockExpense);
    verify(accountRepo).save(mockAccount);
  }

  @Test
  void getExpensesByCategory_ReturnsCorrectExpenses() {
    mockAccount = new Account();
    mockCategory = new Category();
    mockExpenses = Arrays.asList(new Expense(), new Expense());

    when(accountService.checkAccount()).thenReturn(mockAccount);
    when(categoryRepo.findByName("Food")).thenReturn(mockCategory);
    when(expenseRepo.findByCategoryAndAccount(mockCategory, mockAccount)).thenReturn(mockExpenses);
    List<Expense> expenses = expensesController.getExpensesByCategory("Food");

    verify(accountService, times(1)).checkAccount();
    verify(categoryRepo, times(1)).findByName("Food");
    verify(expenseRepo, times(1)).findByCategoryAndAccount(mockCategory, mockAccount);

    assertNotNull(expenses);
    assertEquals(2, expenses.size());
  }

  @Test
  void getExpensesByDate() {
    mockAccount = new Account();
    when(accountService.checkAccount()).thenReturn(mockAccount);
    testDateRequest = new ExpenseDateRequest(LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-31"));

    when(accountService.checkAccount()).thenReturn(mockAccount);
    List<Expense> expectedExpenses = Arrays.asList(new Expense(), new Expense()); // Adjust as per actual Expense class
    when(expenseRepo.findExpensesCreatedBetweenDateAndByAccount(any(), any(), eq(mockAccount)))
        .thenReturn(expectedExpenses);

    List<Expense> result = expensesController.getExpensesByDate(testDateRequest);

    assertEquals(expectedExpenses, result);
  }

  @Test
  void getExpensesByCost() {
    mockAccount = new Account();
    when(accountService.checkAccount()).thenReturn(mockAccount);
    testCostRequest = new ExpenseCostRequest(100L, 500L);

    List<Expense> expectedExpenses = Arrays.asList(new Expense(), new Expense());
    when(expenseRepo.findExpensesByCostAndAccount(anyLong(), anyLong(), eq(mockAccount))).thenReturn(expectedExpenses);

    List<Expense> result = expensesController.getExpensesByCost(testCostRequest);

    assertEquals(expectedExpenses, result);
  }

  @Test
  void getExpensesSumByDate() {
    mockAccount = new Account();
    when(accountService.checkAccount()).thenReturn(mockAccount);
    testDateRequest = new ExpenseDateRequest(LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-31"));

    Long expectedSum = 1000L;
    when(expenseRepo.sumExpensesCreatedBetweenDateAndByAccount(any(), any(), eq(mockAccount))).thenReturn(expectedSum);

    Long result = expensesController.getExpensesSumByDate(testDateRequest);

    assertEquals(expectedSum, result);
  }
}
