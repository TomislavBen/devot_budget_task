package com.example.devottask.enums;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.devottask.entity.Account;
import com.example.devottask.entity.Category;
import com.example.devottask.repository.CategoryRepo;

@Component
public class PredefinedCategories {

  @Autowired
  private CategoryRepo categoryRepo;

  public void createPredefinedCategoriesForAccount(Account account) {
    List<String> categoryNames = Arrays.asList("Food", "Car", "House", "Clothes");
    List<String> descriptions = Arrays.asList("groceries, ordered food, dinner", "Registration, insurance, fuel",
        "Rent, utilities, repairs", "Shoes, shirts, pants");

    for (int i = 0; i < categoryNames.size(); i++) {
      String name = categoryNames.get(i);
      String description = descriptions.get(i);

      Category category = new Category()
          .setName(name)
          .setDescription(description)
          .setAccount(account);

      categoryRepo.save(category);
    }
  }
}
