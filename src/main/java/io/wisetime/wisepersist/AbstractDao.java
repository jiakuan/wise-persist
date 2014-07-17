/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import javax.persistence.EntityManager;

/**
 * @author jiakuanwang
 */
public abstract class AbstractDao {

  private static final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();

  protected EntityManager em() {
    if (threadLocal.get() == null) {
      throw new IllegalStateException(
          "All public DAO methods should be annotated with @Transactional or @NonTransactional");
    }
    return threadLocal.get();
  }

  public void setEntityManager(EntityManager entityManager) {
    if (threadLocal.get() != null && entityManager != null) {
      throw new DaoException(
          "DAO methods annotated with @Transactional or @NonTransactional cannot be nested");
    }
    threadLocal.set(entityManager);
  }

// TODO(Jake): Do we need to support nested methods?
//  public void setEntityManager(EntityManager entityManager, boolean useTransaction) {
//    EntityManager currentEm = threadLocal.get();
//    if (currentEm != null) {
//      if (useTransaction && currentEm.getTransaction().isActive()) {
//        try {
//          currentEm.getTransaction().commit();
//        } catch (Exception e) {
//          log.error(e.getMessage(), e);
//          currentEm.getTransaction().rollback();
//        }
//      }
//      if (currentEm.isOpen()) {
//        currentEm.close();
//      }
//    }
//    threadLocal.set(entityManager);
//  }
}
