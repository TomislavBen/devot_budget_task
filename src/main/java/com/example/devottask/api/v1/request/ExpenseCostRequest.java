package com.example.devottask.api.v1.request;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ExpenseCostRequest {

  @Min(0)
  private Long min;

  @Min(0)
  private Long max;
}
