package com.example.devottask.api.v1.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateExpenseRequest {

  @NotEmpty
  private String category;

  @NotNull
  @Min(0)
  private Long cost;

  private String description;

  private LocalDate transactionDate;
}
