package com.example.devottask.api.v1.request;

import java.util.Optional;

public class UpdateExpenseRequest {

    private String category;

    private Long cost;

    private String description;

   public Optional<String> getCategory() {
    return Optional.ofNullable(category).filter(n -> !n.isEmpty());
  }

  public Optional<String> getDescription() {
    return Optional.ofNullable(description).filter(p -> !p.isEmpty());
  }

  public Optional<Long> getCost() {
    return Optional.ofNullable(cost);
  }
    

}
