/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

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
    TransactionalInterceptor transactionalInterceptor = new TransactionalInterceptor(persistUnit);
    requestInjection(transactionalInterceptor);
    bindInterceptor(
        Matchers.any(),
        Matchers.annotatedWith(Transactional.class),
        transactionalInterceptor
    );

    TransactionalInterceptor
        nonTransactionalInterceptor =
        new TransactionalInterceptor(persistUnit);
    bindInterceptor(
        Matchers.any(),
        Matchers.annotatedWith(NonTransactional.class),
        nonTransactionalInterceptor
    );
  }
}
