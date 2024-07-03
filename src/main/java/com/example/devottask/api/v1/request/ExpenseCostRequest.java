package com.example.devottask.api.v1.request;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class ExpenseCostRequest {

  @Min(0)
  private Long min;

  @Min(0)
  private Long max;
}
