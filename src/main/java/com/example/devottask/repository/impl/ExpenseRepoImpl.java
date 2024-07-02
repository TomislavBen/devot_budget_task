package com.example.devottask.repository.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.devottask.entity.Account;
import com.example.devottask.entity.Expense;
import com.example.devottask.repository.ExpenseRepo;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
public class ExpenseRepoImpl implements ExpenseRepo{

  @PersistenceContext private EntityManager entityManager;

  private CriteriaBuilder cb;

  public ExpenseRepoImpl() {}

  public ExpenseRepoImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @PostConstruct
  public void init() {
    this.cb = entityManager.getCriteriaBuilder();
  }

@Transactional
@Override
public Expense findByIdAndAccount(Long id, Account account) {
    CriteriaQuery<Expense> cq = cb.createQuery(Expense.class);
    Root<Expense> root = cq.from(Expense.class);
    cq.select(root).where(cb.and(
        cb.equal(root.get("id"), id),
        cb.equal(root.get("account"), account)
    ));
    List<Expense> expenses = entityManager.createQuery(cq).getResultList();
    if (expenses.isEmpty()) {
        throw new IllegalArgumentException("No expense found with ID " + id + " and account " + account);
    }
    return expenses.get(0);
}

@Transactional
@Override
public List<Expense> findAllByAccount(Account account) {
    CriteriaQuery<Expense> cq = cb.createQuery(Expense.class);
    Root<Expense> root = cq.from(Expense.class);
    cq.select(root).where(cb.equal(root.get("account"), account));
    List<Expense> expenses = entityManager.createQuery(cq).getResultList();
    return expenses;
}

@Transactional
  @Override
  public Expense save(Expense expense) {
    if (expense.getId() == null) {
      entityManager.persist(expense);
    } else {
      expense = entityManager.merge(expense);
    }

    return expense;
  }

  @Transactional
  @Override
  public void delete(Expense expense) {
    entityManager.remove(expense);
  }

}
