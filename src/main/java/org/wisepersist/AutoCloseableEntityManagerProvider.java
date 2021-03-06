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

import javax.persistence.EntityManagerFactory;

/**
 * An injectable provider of an EntManager object, which implements AutoCloseable to
 * allow it's usage under try-with-resources Java lang feature.
 *
 * @author thomas.haines.
 */
public class AutoCloseableEntityManagerProvider {

  private final EntityManagerFactory emf;

  public AutoCloseableEntityManagerProvider(EntityManagerFactory emf) {
    this.emf = emf;
  }

  public AutoCloseableEntityManager get() {
    return new AutoCloseableEntityManager(emf.createEntityManager());
  }

}
