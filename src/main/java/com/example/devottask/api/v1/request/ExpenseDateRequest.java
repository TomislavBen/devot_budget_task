package com.example.devottask.api.v1.request;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ExpenseDateRequest {

  private LocalDate startDate;
  private LocalDate endDate;
}
