/*
 * Copyright (c) 2014 WisePersist
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wisepersist;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import java.util.Map;

import javax.persistence.EntityManagerFactory;

/**
 * Wrapper to inject EntityManagerFactory objects as required.
 */
public class WisePersistModule extends AbstractModule {

  private final EntityManagerFactory emf;

  public WisePersistModule(String persistUnit) {
    this(EntityManagerFactoryProvider.get(persistUnit));
  }

  public WisePersistModule(String persistUnit, Map<String, Object> additionalProperties) {
    this(EntityManagerFactoryProvider.get(persistUnit, null, additionalProperties));
  }

  public WisePersistModule(String persistUnit, DataSourceProvider dsProvider) {
    this(EntityManagerFactoryProvider.get(persistUnit, dsProvider, null));
  }

  /**
   * @param additionalProperties Any properties specified in this map will override the default
   *                             settings defined in persistUnit.
   */
  public WisePersistModule(String persistUnit, DataSourceProvider dsProvider,
                           Map<String, Object> additionalProperties) {
    this(EntityManagerFactoryProvider.get(persistUnit, dsProvider, additionalProperties));
  }

  /**
   * @param emf custom entity manager factory.
   * @deprecated this method will be removed in future releases.
   */
  @Deprecated
  public WisePersistModule(EntityManagerFactory emf) {
    Preconditions.checkArgument(emf != null, "EntityManagerFactory cannot be null");
    this.emf = emf;
  }

  @Override
  protected void configure() {
    bind(EntManagerProvider.class).toInstance(new EntManagerProvider(emf));

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
      throw new DaoException(
          "Only one WisePersistModule can be created in one Guice injector. " +
          "If you need to access multiple data sources, " +
          "please create multiple separate Guice injectors.");
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
