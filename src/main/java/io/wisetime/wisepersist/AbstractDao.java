/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import javax.persistence.EntityManager;

/**
 * @author jiakuanwang
 */
public abstract class AbstractDao {

  private EntityManager entityManager;

  protected EntityManager getEntityManager() {
    if (entityManager == null) {
      throw new IllegalStateException(
          "All public DAO methods should be annotated with @Transactional or @NonTransactional");
    }
    return entityManager;
  }

  public void setEntityManager(EntityManager entityManager) {
    if (this.entityManager != null && entityManager != null) {
      throw new IllegalStateException(
          "DAO methods annotated with @Transactional or @NonTransactional cannot be nested");
    }
    this.entityManager = entityManager;
  }
}
