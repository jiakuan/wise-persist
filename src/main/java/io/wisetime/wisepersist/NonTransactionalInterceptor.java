/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

/**
 * @author jiakuanwang
 */
public class NonTransactionalInterceptor extends DaoMethodInterceptor {

  private static Logger log = LoggerFactory.getLogger(NonTransactionalInterceptor.class);

  public NonTransactionalInterceptor(String persistUnit) {
    super(persistUnit);
  }

  @Override
  protected Object invokeWithEntityManager(MethodInvocation invocation, EntityManager entityManager)
      throws Throwable {
    try {
      return invocation.proceed();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      if (entityManager != null && entityManager.isOpen()) {
        entityManager.close();
      }
    }
  }
}
