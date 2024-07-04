package com.example.devottask.api.v1.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.devottask.api.v1.request.CreateExpenseRequest;
import com.example.devottask.api.v1.request.ExpenseCostRequest;
import com.example.devottask.api.v1.request.ExpenseDateRequest;
import com.example.devottask.api.v1.request.UpdateExpenseRequest;
import com.example.devottask.entity.Account;
import com.example.devottask.entity.Category;
import com.example.devottask.entity.Expense;
import com.example.devottask.repository.AccountRepo;
import com.example.devottask.repository.CategoryRepo;
import com.example.devottask.repository.ExpenseRepo;
import com.example.devottask.service.AccountService;
import com.example.devottask.service.ExpensesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpensesController {

  private final AccountService accountService;

  private final ExpensesService expensesService;

  private final ExpenseRepo expenseRepo;

  private final AccountRepo accountRepo;

  private final CategoryRepo categoryRepo;

  @PostMapping
  public ResponseEntity<Expense> createExpense(@Valid @RequestBody CreateExpenseRequest createExpenseRequest)
      throws Exception {
    Account account = accountService.checkAccount();
    Category category = expensesService.getCategoryByNameForAccount(createExpenseRequest.getCategory());

    Expense expense = new Expense()
        .setCategory(category)
        .setCost(createExpenseRequest.getCost())
        .setDescription(createExpenseRequest.getDescription())
        .setTransactionDate(createExpenseRequest.getTransactionDate())
        .setAccount(account);

    account.reduceBalance(expense.getCost());

    accountRepo.save(account);
    expenseRepo.save(expense);

    return ResponseEntity.ok(expense);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
    Expense expense = expenseRepo.findByIdAndAccount(id, accountService.checkAccount());

    return ResponseEntity.ok(expense);
  }

  @GetMapping
  public ResponseEntity<List<Expense>> getAllExpenses() {
    List<Expense> expense = expenseRepo.findAllByAccount(accountService.checkAccount());

    return ResponseEntity.ok(expense);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Expense> updateExpense(@PathVariable Long id,
      @RequestBody UpdateExpenseRequest updateExpenseRequest) throws Exception {
    Account account = accountService.checkAccount();
    Expense expense = expenseRepo.findByIdAndAccount(id, account);
    Optional<Category> category = expensesService
        .getCategoryByNameForAccountOptional(updateExpenseRequest.getCategory());

    category.ifPresent(expense::setCategory);
    updateExpenseRequest.getDescription().ifPresent(expense::setDescription);
    updateExpenseRequest.getTransactionDate().ifPresent(expense::setTransactionDate);

    updateExpenseRequest.getCost().ifPresent(newCost -> {
      Long originalCost = expense.getCost();
      account.adjustBalanceForExpenseUpdate(originalCost, newCost);
      expense.setCost(newCost);
    });

    accountRepo.save(account);
    expenseRepo.save(expense);

    return ResponseEntity.ok(expense);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
    Account account = accountService.checkAccount();
    Expense expense = expenseRepo.findByIdAndAccount(id, account);

    account.setBalance(account.getBalance() + expense.getCost());
    accountRepo.save(account);

    expenseRepo.delete(expense);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/by-category")
  public List<Expense> getExpensesByCategory(@RequestParam String categoryName) {
    Account account = accountService.checkAccount();
    Category category = categoryRepo.findByNameAndAccount(categoryName, account);

    return expenseRepo.findByCategoryAndAccount(category, account);
  }

  @PostMapping("/by-date")
  public List<Expense> getExpensesByDate(@RequestBody ExpenseDateRequest dateRequest) {
    Account account = accountService.checkAccount();

    return expenseRepo.findExpensesCreatedBetweenDateAndByAccount(dateRequest.getStartDate(), dateRequest.getEndDate(),
        account);
  }

  @PostMapping("/by-cost")
  public List<Expense> getExpensesByCost(@Valid @RequestBody ExpenseCostRequest expensesCostRequest) {
    Account account = accountService.checkAccount();

    return expenseRepo.findExpensesByCostAndAccount(expensesCostRequest.getMin(), expensesCostRequest.getMax(),
        account);
  }

  @PostMapping("/sum-by-date")
  public Long getExpensesSumByDate(@RequestBody ExpenseDateRequest dateRequest) {
    Account account = accountService.checkAccount();

    return expenseRepo.sumExpensesCreatedBetweenDateAndByAccount(dateRequest.getStartDate(), dateRequest.getEndDate(),
        account);
  }
}
