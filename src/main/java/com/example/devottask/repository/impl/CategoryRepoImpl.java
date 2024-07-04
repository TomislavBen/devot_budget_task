package com.example.devottask.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.devottask.entity.Account;
import com.example.devottask.entity.Category;
import com.example.devottask.repository.CategoryRepo;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
public class CategoryRepoImpl implements CategoryRepo {

  @PersistenceContext
  private EntityManager entityManager;

  private CriteriaBuilder cb;

  public CategoryRepoImpl() {
  }

  public CategoryRepoImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @PostConstruct
  public void init() {
    this.cb = entityManager.getCriteriaBuilder();
  }

  @Transactional
  @Override
  public Category findByNameAndAccount(String name, Account account) {
    CriteriaQuery<Category> cq = cb.createQuery(Category.class);
    Root<Category> root = cq.from(Category.class);
    cq.select(root).where(cb.and(
        cb.equal(root.get("name"), name),
        cb.equal(root.get("account"), account)));
    List<Category> categories = entityManager.createQuery(cq).getResultList();

    return categories.isEmpty() ? null : categories.get(0);
  }

  @Transactional
  @Override
  public Category findByNameAndAccountOptional(Optional<String> nameOpt, Account account) {
    if (!nameOpt.isPresent() || nameOpt.get().trim().isEmpty()) {
      return null;
    }

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Category> cq = cb.createQuery(Category.class);
    Root<Category> root = cq.from(Category.class);
    List<Predicate> predicates = new ArrayList<>();

    nameOpt.ifPresent(name -> predicates.add(cb.equal(root.get("name"), name)));
    predicates.add(cb.equal(root.get("account"), account));

    cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
    List<Category> categories = entityManager.createQuery(cq).getResultList();

    return categories.isEmpty() ? null : categories.get(0);
  }

  @Transactional
  @Override
  public Category findByIdAndAccount(Long id, Account account) {
    CriteriaQuery<Category> cq = cb.createQuery(Category.class);
    Root<Category> root = cq.from(Category.class);
    cq.select(root).where(cb.and(
        cb.equal(root.get("id"), id),
        cb.equal(root.get("account"), account)));
    List<Category> categories = entityManager.createQuery(cq).getResultList();
    if (categories.isEmpty()) {
      throw new IllegalArgumentException("No category found with given ID and account");
    }

    return categories.get(0);
  }

  @Transactional
  @Override
  public List<Category> findAllByAccount(Account account) {
    CriteriaQuery<Category> cq = cb.createQuery(Category.class);
    Root<Category> root = cq.from(Category.class);
    cq.select(root).where(cb.equal(root.get("account"), account));
    List<Category> categories = entityManager.createQuery(cq).getResultList();

    return categories;
  }

  @Transactional
  @Override
  public Category save(Category category) {
    if (category.getId() == null) {
      entityManager.persist(category);
    } else {
      category = entityManager.merge(category);
    }

    return category;
  }

  @Override
  public Category findByName(String name) {
    CriteriaQuery<Category> cq = cb.createQuery(Category.class);
    Root<Category> root = cq.from(Category.class);
    cq.select(root).where(cb.equal(root.get("name"), name));
    List<Category> categories = entityManager.createQuery(cq).getResultList();

    if (categories.isEmpty()) {
      throw new IllegalArgumentException("Category not found");
    }

    return categories.isEmpty() ? null : categories.get(0);
  }

  @Transactional
  @Override
  public void delete(Category category) {
    entityManager.remove(category);
  }
}
