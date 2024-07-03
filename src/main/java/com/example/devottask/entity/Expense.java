package com.example.devottask.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "expense")
@Accessors(chain = true)
@Data
public class Expense {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ManyToOne
  private Account account;

  @NotNull
  @ManyToOne
  private Category category;

  @NotNull
  private Long cost;

  private String description;

  @Column(name = "transaction_date")
  private LocalDate transactionDate;

  @PrePersist
  public void prePersist() {
    if (transactionDate == null) {
      transactionDate = LocalDate.now();
    }
  }
}
