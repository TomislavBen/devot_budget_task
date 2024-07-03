package com.example.devottask.api.v1.controller;

import com.example.devottask.api.v1.request.CreateCategoryRequest;
import com.example.devottask.api.v1.request.UpdateCategoryRequest;
import com.example.devottask.entity.Account;
import com.example.devottask.entity.Category;
import com.example.devottask.repository.CategoryRepo;
import com.example.devottask.repository.ExpenseRepo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.example.devottask.service.AccountService;
import com.example.devottask.service.CategoryService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final AccountService accountService;

  private final CategoryService categoryService;

  private final CategoryRepo categoryRepo;

  private final ExpenseRepo expenseRepo;

  @PostMapping
  public ResponseEntity<Category> createCategory(@Valid @RequestBody CreateCategoryRequest CreateCategoryRequest)
      throws Exception {
    Account account = accountService.checkAccount();

    categoryService.checkCategoryByNameForAccount(CreateCategoryRequest.getName());

    Category category = new Category()
        .setName(CreateCategoryRequest.getName())
        .setDescription(CreateCategoryRequest.getDescription())
        .setAccount(account);
        
    categoryRepo.save(category);

    return ResponseEntity.ok(category);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
    Category category = categoryRepo.findByIdAndAccount(id, accountService.checkAccount());
    return ResponseEntity.ok(category);
  }

  @GetMapping
  public ResponseEntity<List<Category>> getAllCategories() {
    List<Category> categories = categoryRepo.findAllByAccount(accountService.checkAccount());
    return ResponseEntity.ok(categories);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Category> updateCategory(@PathVariable Long id,
      @RequestBody UpdateCategoryRequest updateCategoryRequest) throws Exception {
    Category category = categoryRepo.findByIdAndAccount(id, accountService.checkAccount());

    updateCategoryRequest.getName().ifPresent(category::setName);
    updateCategoryRequest.getDescription().ifPresent(category::setDescription);

    categoryRepo.save(category);

    return ResponseEntity.ok(category);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
    Account account = accountService.checkAccount();
    Category category = categoryRepo.findByIdAndAccount(id, account);

    if (!expenseRepo.findByCategoryAndAccount(category, account).isEmpty()) {
      throw new IllegalArgumentException("Cannot delete category as it has associated expenses.");
    }
    categoryRepo.delete(category);

    return ResponseEntity.ok().build();
  }
}
