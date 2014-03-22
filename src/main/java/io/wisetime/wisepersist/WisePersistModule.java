/*
 * Copyright 2014 WiseTime Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    bindInterceptor(
        Matchers.any(),
        Matchers.annotatedWith(Transactional.class),
        new TransactionalInterceptor()
    );
  }
}
