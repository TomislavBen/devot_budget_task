package com.example.devottask.api.v1.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.devottask.api.v1.request.CreateCategoryRequest;
import com.example.devottask.api.v1.request.UpdateCategoryRequest;
import com.example.devottask.entity.Account;
import com.example.devottask.entity.Category;
import com.example.devottask.repository.CategoryRepo;
import com.example.devottask.repository.ExpenseRepo;
import com.example.devottask.service.AccountService;
import com.example.devottask.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

@SpringBootTest
public class CategoryControllerTest {

  @Mock
  private AccountService accountService;

  @Mock
  private CategoryService categoryService;

  @Mock
  private CategoryRepo categoryRepo;

  @Mock
  private ExpenseRepo expenseRepo;

  @InjectMocks
  private CategoryController categoryController;

  private Account mockAccount;
  private Category mockCategory;

  @BeforeEach
  void setUp() {
    mockAccount = new Account();
    mockCategory = new Category();
  }

  @Test
  void testCreateCategory() throws Exception {
    // Setup
    CreateCategoryRequest request = new CreateCategoryRequest();
    request.setName("Test Category");
    request.setDescription("Test Description");

    when(accountService.checkAccount()).thenReturn(mockAccount);
    when(categoryRepo.save(any(Category.class))).thenReturn(mockCategory);

    // Execute
    ResponseEntity<Category> response = categoryController.createCategory(request);

    // Verify
    assertEquals(200, response.getStatusCode().value());
    verify(categoryRepo, times(1)).save(any(Category.class));
  }

  @Test
  void testGetCategoryById() {
    // Setup
    Long categoryId = 1L;
    when(accountService.checkAccount()).thenReturn(mockAccount);
    when(categoryRepo.findByIdAndAccount(categoryId, mockAccount)).thenReturn(mockCategory);

    // Execute
    ResponseEntity<Category> response = categoryController.getCategoryById(categoryId);

    // Verify
    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  void testGetAllCategories() {
    // Setup
    when(accountService.checkAccount()).thenReturn(mockAccount);
    when(categoryRepo.findAllByAccount(mockAccount)).thenReturn(Collections.singletonList(mockCategory));

    // Execute
    ResponseEntity<List<Category>> response = categoryController.getAllCategories();

    // Verify
    assertEquals(200, response.getStatusCode().value());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void testUpdateCategory() throws Exception {
    // Setup
    Long categoryId = 1L;
    UpdateCategoryRequest request = new UpdateCategoryRequest();
    request.setName("Updated Name");
    request.setDescription("Updated Description");

    when(accountService.checkAccount()).thenReturn(mockAccount);
    when(categoryRepo.findByIdAndAccount(categoryId, mockAccount)).thenReturn(mockCategory);

    // Execute
    ResponseEntity<Category> response = categoryController.updateCategory(categoryId, request);

    // Verify
    assertEquals(200, response.getStatusCode().value());
    verify(categoryRepo, times(1)).save(mockCategory);
  }

  @Test
  void testDeleteCategory() {
    // Setup
    Long categoryId = 1L;
    when(accountService.checkAccount()).thenReturn(mockAccount);
    when(categoryRepo.findByIdAndAccount(categoryId, mockAccount)).thenReturn(mockCategory);
    when(expenseRepo.findByCategoryAndAccount(mockCategory, mockAccount)).thenReturn(Collections.emptyList());

    // Execute
    ResponseEntity<?> response = categoryController.deleteCategory(categoryId);

    // Verify
    assertEquals(200, response.getStatusCode().value());
    verify(categoryRepo, times(1)).delete(mockCategory);
  }
}
