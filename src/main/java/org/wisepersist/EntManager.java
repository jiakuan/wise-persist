package org.wisepersist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

/**
 * Wrapper to allow try with resources connected with EntityManager.
 *
 * @author thomas.haines.
 */
public class EntManager implements AutoCloseable {

  private static final Logger log = LoggerFactory.getLogger(EntManager.class);

  private final EntityManager em;

  public EntManager(EntityManager em) {
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
