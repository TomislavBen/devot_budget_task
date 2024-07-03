package com.example.devottask.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.devottask.entity.Account;
import com.example.devottask.entity.Category;
import com.example.devottask.entity.Expense;

public interface ExpenseRepo {

  Expense save(Expense expense);

  Expense findByIdAndAccount(Long id, Account account);

  List<Expense> findAllByAccount(Account account);

  void delete(Expense expense);

  List<Expense> findByCategoryAndAccount(Category category, Account account);

  List<Expense> findExpensesCreatedBetweenDateAndByAccount(LocalDate startDate, LocalDate endDate, Account account);

  List<Expense> findExpensesByCostAndAccount(Long min, Long max, Account account);

  Long sumExpensesCreatedBetweenDateAndByAccount(LocalDate startDate, LocalDate endDate, Account account);
}
