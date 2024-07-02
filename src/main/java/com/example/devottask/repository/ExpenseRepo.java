package com.example.devottask.repository;

import java.util.List;

import com.example.devottask.entity.Account;
import com.example.devottask.entity.Expense;

public interface ExpenseRepo {

  public Expense save(Expense expense);

  public Expense findByIdAndAccount(Long id, Account account);

  public List<Expense> findAllByAccount(Account account);

  public void delete(Expense expense);

}
