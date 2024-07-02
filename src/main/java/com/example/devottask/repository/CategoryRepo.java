package com.example.devottask.repository;

import java.util.List;
import java.util.Optional;

import com.example.devottask.entity.Account;
import com.example.devottask.entity.Category;

public interface CategoryRepo {

  public Category save(Category category);

  public Category findByName(String name);

  public Category findByNameAndAccount(String name, Account account);

  public Category findByIdAndAccount(Long id, Account account);

  public List<Category> findAllByAccount(Account account);

  public void delete(Category category);

  public Category findByNameAndAccountOptional(Optional<String> nameOpt, Account account);

}
