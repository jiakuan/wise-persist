/*
 * Copyright (c) 2016 WisePersist
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

/**
 * Wrapper to allow try with resources connected with EntityManager.
 *
 * @author thomas.haines.
 */
public class AutoCloseableEntityManager implements AutoCloseable {

  private static final Logger log = LoggerFactory.getLogger(AutoCloseableEntityManager.class);

  private final EntityManager em;

  public AutoCloseableEntityManager(EntityManager em) {
    this.em = em;
  }

  public EntityManager em() {
    return em;
  }

  @Override
  public void close() {
    if (em != null && em.isOpen()) {
      try {
        em.close();
      } catch (Exception e) {
        log.warn("Error closing entity manager", e);
      }
    }
  }
}
