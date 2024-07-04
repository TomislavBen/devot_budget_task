// package com.example.devottask.service;

// import java.util.Optional;

// import org.springframework.stereotype.Service;

// import com.example.devottask.entity.Account;
// import com.example.devottask.entity.Category;
// import com.example.devottask.repository.CategoryRepo;

// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class ExpensesService {

//   private final CategoryRepo categoryRepo;
//   private final AccountService accountService;

//   public Category getCategoryByNameForAccount(String categoryName) {
//     Account currentAccount = accountService.checkAccount();
//     Category category = categoryRepo.findByNameAndAccount(categoryName, currentAccount);
//     if (category == null) {
//       throw new IllegalArgumentException("Category with that name does not exist");
//     }
//     return category;
//   }

//   public Optional<Category> getCategoryByNameForAccountOptional(Optional<String> categoryName) {
//     Account currentAccount = accountService.checkAccount();
//     return Optional.ofNullable(categoryRepo.findByNameAndAccountOptional(categoryName, currentAccount));
//   }
// }
