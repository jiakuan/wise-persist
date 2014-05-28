/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Wrapper to inject EntityManagerFactory objects as required.
 */
public class WisePersistModule extends AbstractModule {

  private final String persistUnit;

  public WisePersistModule(String persistUnit) {
    this.persistUnit = persistUnit;
  }

  @Override
  protected void configure() {
    bindConstant().annotatedWith(PersistUnit.class).to(persistUnit);

    bind(EntityManagerFactory.class)
        .toProvider(EntityManagerFactoryProvider.class).in(Singleton.class);

    bind(EntityManager.class).toProvider(EntityManagerProvider.class);

    TransactionalInterceptor transactionalInterceptor = new TransactionalInterceptor();
    requestInjection(transactionalInterceptor);
    bindInterceptor(
        Matchers.any(),
        Matchers.annotatedWith(Transactional.class),
        transactionalInterceptor
    );

    TransactionalInterceptor nonTransactionalInterceptor = new TransactionalInterceptor();
    bindInterceptor(
        Matchers.any(),
        Matchers.annotatedWith(NonTransactional.class),
        nonTransactionalInterceptor
    );
  }
}
