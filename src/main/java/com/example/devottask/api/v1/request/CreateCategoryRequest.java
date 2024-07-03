package com.example.devottask.api.v1.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateCategoryRequest {

  @NotEmpty
  private String name;

  private String description;
}
