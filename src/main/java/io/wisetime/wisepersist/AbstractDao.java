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
          "All public methods should be annotated with @Transactional or @NonTransactional");
    }
    return entityManager;
  }

  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
}
