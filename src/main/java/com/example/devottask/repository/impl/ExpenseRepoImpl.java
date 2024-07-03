package com.example.devottask.repository.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.devottask.entity.Account;
import com.example.devottask.entity.Category;
import com.example.devottask.entity.Expense;
import com.example.devottask.repository.ExpenseRepo;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import jakarta.persistence.criteria.Predicate;

@Repository
public class ExpenseRepoImpl implements ExpenseRepo {

  @PersistenceContext
  private EntityManager entityManager;

  private CriteriaBuilder cb;

  public ExpenseRepoImpl() {
  }

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
        cb.equal(root.get("account"), account)));
    List<Expense> expenses = entityManager.createQuery(cq).getResultList();
    if (expenses.isEmpty()) {
      throw new IllegalArgumentException("No expense found with given ID and account");
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
  public List<Expense> findByCategoryAndAccount(Category category, Account account) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Expense> cq = cb.createQuery(Expense.class);
    Root<Expense> root = cq.from(Expense.class);

    Predicate categoryPredicate = cb.equal(root.get("category"), category);
    Predicate accountPredicate = cb.equal(root.get("account"), account);

    cq.where(cb.and(categoryPredicate, accountPredicate));

    List<Expense> expenses = entityManager.createQuery(cq).getResultList();

    return expenses;
  }

  @Override
  public List<Expense> findExpensesCreatedBetweenDateAndByAccount(LocalDate startDate, LocalDate endDate,
      Account account) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Expense> query = criteriaBuilder.createQuery(Expense.class);
    Root<Expense> root = query.from(Expense.class);
    query.select(root);

    Predicate condition1 = startDate != null
        ? criteriaBuilder.greaterThanOrEqualTo(
            root.get("transactionDate"), startDate.atStartOfDay())
        : criteriaBuilder.conjunction();

    Predicate condition2 = endDate != null
        ? criteriaBuilder.lessThanOrEqualTo(
            root.get("transactionDate"),
            endDate.plusDays(1).atStartOfDay())
        : criteriaBuilder.conjunction();

    Predicate condition3 = criteriaBuilder.equal(root.get("account"), account);

    query.where(criteriaBuilder.and(condition1, condition2, condition3));

    return entityManager.createQuery(query).getResultList();
  }

  @Override
  public List<Expense> findExpensesByCostAndAccount(Long min, Long max, Account account) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Expense> query = criteriaBuilder.createQuery(Expense.class);
    Root<Expense> root = query.from(Expense.class);
    query.select(root);

    Predicate condition1 = min != null
        ? criteriaBuilder.greaterThanOrEqualTo(
            root.get("cost"), min)
        : criteriaBuilder.conjunction();

    Predicate condition2 = max != null
        ? criteriaBuilder.lessThanOrEqualTo(
            root.get("cost"),
            max)
        : criteriaBuilder.conjunction();

    Predicate condition3 = criteriaBuilder.equal(root.get("account"), account);

    query.where(criteriaBuilder.and(condition1, condition2, condition3));

    return entityManager.createQuery(query).getResultList();
  }

  @Override
  public Long sumExpensesCreatedBetweenDateAndByAccount(LocalDate startDate, LocalDate endDate, Account account) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
    Root<Expense> root = query.from(Expense.class);

    Expression<Long> sumCost = criteriaBuilder.sum(root.get("cost"));

    Predicate condition1 = startDate != null
        ? criteriaBuilder.greaterThanOrEqualTo(
            root.get("transactionDate"), startDate.atStartOfDay())
        : criteriaBuilder.conjunction();

    Predicate condition2 = endDate != null
        ? criteriaBuilder.lessThanOrEqualTo(
            root.get("transactionDate"),
            endDate.plusDays(1).atStartOfDay())
        : criteriaBuilder.conjunction();

    Predicate condition3 = criteriaBuilder.equal(root.get("account"), account);

    query.select(sumCost).where(criteriaBuilder.and(condition1, condition2, condition3));

    Long result = entityManager.createQuery(query).getSingleResult();

    return result != null ? result : 0;
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
