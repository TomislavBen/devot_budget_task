package com.example.devottask.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import org.springframework.stereotype.Repository;

import com.example.devottask.entity.Account;
import com.example.devottask.repository.AccountRepo;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
public class AccountRepoImpl implements AccountRepo {

  @PersistenceContext
  private EntityManager entityManager;

  private CriteriaBuilder cb;

  public AccountRepoImpl() {
  }

  @PostConstruct
  public void init() {
    this.cb = entityManager.getCriteriaBuilder();
  }

  @Transactional
  @Override
  public String checkIfMailInUse(String email) {
    if (findByEmail(email).isPresent()) {
      throw new IllegalArgumentException("Email is already in use");
    }

    return email;
  }

  @Transactional
  @Override
  public Optional<String> checkIfMailInUseOptional(String email) {
    if (findByEmail(email).isPresent()) {
      throw new IllegalArgumentException("Email is already in use");
    }

    return Optional.ofNullable(email);
  }

  @Transactional
  @Override
  public boolean doesAccountExistByEmail(String email) {
    
    return findByEmail(email).isPresent();
  }

  @Transactional
  @Override
  public List<Account> findAll() {
    CriteriaQuery<Account> cq = cb.createQuery(Account.class);
    Root<Account> root = cq.from(Account.class);
    cq.select(root);

    return entityManager.createQuery(cq).getResultList();
  }

  @Transactional
  @Override
  public Optional<Account> findByEmail(String email) {
    CriteriaQuery<Account> cq = this.findByEmailCQ(email);
    TypedQuery<Account> query = entityManager.createQuery(cq);

    try {
      return Optional.ofNullable(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Transactional
  @Override
  public Account findById(Long id) {
    Account account = entityManager.find(Account.class, id);
    if (account == null) {
      throw new IllegalArgumentException("Account with given id not found");
    }

    return account;
  }

  @Transactional
  @Override
  public Account save(Account user) {
    if (user.getId() == null) {
      entityManager.persist(user);
    } else {
      user = entityManager.merge(user);
    }

    return user;
  }

  private <T> CriteriaQuery<T> createCriteriaQuery(
      Class<T> entityClass, BiFunction<CriteriaBuilder, Root<T>, Predicate> predicateFunction) {
    CriteriaQuery<T> cq = cb.createQuery(entityClass);
    Root<T> root = cq.from(entityClass);
    cq.select(root).where(predicateFunction.apply(cb, root));

    return cq;
  }

  private CriteriaQuery<Account> findByEmailCQ(String email) {
    return this.createCriteriaQuery(
        Account.class, (cb, root) -> cb.equal(root.get("email"), email));
  }
}
