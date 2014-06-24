/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import javax.persistence.EntityManagerFactory;

/**
 * Wrapper to inject EntityManagerFactory objects as required.
 */
public class WisePersistModule extends AbstractModule {

  private final String persistUnit;
  private final EntityManagerFactory emf;

  public WisePersistModule(String persistUnit) {
    Preconditions.checkArgument(
        !Strings.isNullOrEmpty(persistUnit), "persistUnit cannot be null or empty");
    this.persistUnit = persistUnit;
    this.emf = null;
  }

  public WisePersistModule(EntityManagerFactory emf) {
    Preconditions.checkArgument(emf != null, "EntityManagerFactory cannot be null");
    this.persistUnit = null;
    this.emf = emf;
  }


  @Override
  protected void configure() {
    TransactionalInterceptor transactionalInterceptor =
        emf != null
        ? new TransactionalInterceptor(emf)
        : new TransactionalInterceptor(persistUnit);
    requestInjection(transactionalInterceptor);
    bindInterceptor(
        Matchers.any(),
        Matchers.annotatedWith(Transactional.class),
        transactionalInterceptor
    );

    NonTransactionalInterceptor nonTransactionalInterceptor =
        emf != null
        ? new NonTransactionalInterceptor(emf)
        : new NonTransactionalInterceptor(persistUnit);
    bindInterceptor(
        Matchers.any(),
        Matchers.annotatedWith(NonTransactional.class),
        nonTransactionalInterceptor
    );
  }
}
