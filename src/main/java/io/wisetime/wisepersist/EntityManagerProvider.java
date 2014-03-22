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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author jiakuanwang
 */
public class EntityManagerProvider implements Provider<EntityManager> {

  private static final Logger log = LoggerFactory.getLogger(EntityManagerProvider.class);

  @Inject
  private EntityManagerFactory emf;

  @Override
  public EntityManager get() {
    log.debug("Creating a new entity manager...");
    return emf.createEntityManager();
  }
}
