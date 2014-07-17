/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import javax.persistence.EntityManagerFactory;

/**
 * Wrapper to inject EntityManagerFactory objects as required.
 */
public class WisePersistModule extends AbstractModule {

  private final EntityManagerFactory emf;

  public WisePersistModule(String persistUnit) {
    this(EntityManagerFactoryProvider.get(persistUnit));
  }

  public WisePersistModule(EntityManagerFactory emf) {
    Preconditions.checkArgument(emf != null, "EntityManagerFactory cannot be null");
    this.emf = emf;
  }

  @Override
  protected void configure() {
    DaoMethodInterceptor transactionalInterceptor = new DaoMethodInterceptor(emf, true);
    requestInjection(transactionalInterceptor);
    bindInterceptor(
        Matchers.any(), Matchers.annotatedWith(Transactional.class), transactionalInterceptor);

    DaoMethodInterceptor nonTransactionalInterceptor = new DaoMethodInterceptor(emf, false);
    bindInterceptor(
        Matchers.any(), Matchers.annotatedWith(NonTransactional.class),
        nonTransactionalInterceptor);
  }

  @Override
  public boolean equals(Object o) {
    // Make sure each Guice injector can only have one WisePersistModule, and multiple
    // WisePersistModule instances can exist in different Guice injector
    // http://stackoverflow.com/questions/20735211/ensure-module-is-loaded-only-once-in-guice
    // http://www.mattinsler.com/post/26548709502/google-guice-module-de-duplication
    boolean equals = (this == o) || !(o == null || getClass() != o.getClass());
    if (equals) {
      throw new DaoException("Only one WisePersistModule can be created");
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
