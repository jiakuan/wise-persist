/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

/**
 * @author jiakuanwang
 */
public abstract class AbstractDao {

  private static final Logger log = LoggerFactory.getLogger(AbstractDao.class);

  private static final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();

  protected EntityManager em() {
    if (threadLocal.get() == null) {
      throw new IllegalStateException(
          "All public DAO methods should be annotated with @Transactional or @NonTransactional");
    }
    return threadLocal.get();
  }

  public void setEntityManager(EntityManager entityManager, boolean useTransaction) {
    EntityManager currentEm = threadLocal.get();
    if (currentEm != null) {
      if (useTransaction && currentEm.getTransaction().isActive()) {
        try {
          currentEm.getTransaction().commit();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          currentEm.getTransaction().rollback();
        }
      }
      if (currentEm.isOpen()) {
        currentEm.close();
      }
    }
    threadLocal.set(entityManager);
  }
}
