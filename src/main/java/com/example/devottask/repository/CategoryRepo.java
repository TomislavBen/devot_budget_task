package com.example.devottask.repository;

import java.util.List;
import java.util.Optional;

import com.example.devottask.entity.Account;
import com.example.devottask.entity.Category;

public interface CategoryRepo {

  Category save(Category category);

  Category findByName(String name);

  Category findByNameAndAccount(String name, Account account);

  Category findByIdAndAccount(Long id, Account account);

  List<Category> findAllByAccount(Account account);

  void delete(Category category);

  Category findByNameAndAccountOptional(Optional<String> nameOpt, Account account);
}
