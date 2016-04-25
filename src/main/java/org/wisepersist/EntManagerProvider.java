package org.wisepersist;

import javax.persistence.EntityManagerFactory;

/**
 * An injectable provider of an EntManager object, which implements AutoCloseable to allow it's usage under
 * try-with-resources Java lang feature.
 *
 * @author thomas.haines.
 */
public class EntManagerProvider {

  private final EntityManagerFactory emf;

  public EntManagerProvider(EntityManagerFactory emf) {
    this.emf = emf;
  }

  public EntManager get() {
    return new EntManager(emf.createEntityManager());
  }

}
