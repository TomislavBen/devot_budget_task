package com.example.devottask.api.v1.request;

import java.util.Optional;

import lombok.Data;

@Data
public class UpdateCategoryRequest {

  private String name;
  
  private String description;

  public Optional<String> getName() {
    return Optional.ofNullable(name).filter(n -> !n.isEmpty());
  }

  public Optional<String> getDescription() {
    return Optional.ofNullable(description).filter(p -> !p.isEmpty());
  }

}
