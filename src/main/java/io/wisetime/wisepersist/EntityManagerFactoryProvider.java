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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author jiakuanwang
 */
@Singleton
public class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {

  private static final Logger log = LoggerFactory.getLogger(EntityManagerFactoryProvider.class);

  private final String persistUnit;

  @Inject
  public EntityManagerFactoryProvider(@PersistUnit String persistUnit) {
    this.persistUnit = persistUnit;
  }

  @Override
  public EntityManagerFactory get() {
    final EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistUnit);
    log.info("Initialized entity manager factory.");
    return emf;
  }
}
