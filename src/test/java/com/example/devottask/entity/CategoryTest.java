package com.example.devottask.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

  private Category category;

  @BeforeEach
  void setUp() {
    category = new Category();
  }

  @Test
  void testCategoryCreation() {
    category.setId(1L).setName("Electronics").setDescription("Electronic items");
    assertNotNull(category);
    assertEquals(1L, category.getId());
    assertEquals("Electronics", category.getName());
    assertEquals("Electronic items", category.getDescription());
  }

  @Test
  void testCategoryFieldAccessors() {
    // Testing ID field
    category.setId(2L);
    assertEquals(2L, category.getId());

    // Testing name field
    category.setName("Books");
    assertEquals("Books", category.getName());

    // Testing description field
    category.setDescription("All kinds of books");
    assertEquals("All kinds of books", category.getDescription());
  }
}
