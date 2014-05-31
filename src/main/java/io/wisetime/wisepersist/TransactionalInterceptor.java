/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;

/**
 * @author jiakuanwang
 */
public class TransactionalInterceptor extends DaoMethodInterceptor {

  private static Logger log = LoggerFactory.getLogger(TransactionalInterceptor.class);

  public TransactionalInterceptor(String persistUnit) {
    super(persistUnit);
  }


  @Override
  protected Object invokeWithEntityManager(MethodInvocation invocation, EntityManager entityManager)
      throws Throwable {
    try {
      entityManager.getTransaction().begin();
      Object result = invocation.proceed();
      entityManager.getTransaction().commit();

      return result;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      entityManager.getTransaction().rollback();

      Method method = invocation.getMethod();
      throw new DaoException(String.format(
          "Failed to execute %s.%s, rolled back",
          method.getDeclaringClass(), method.getName()), e);
    } finally {
      if (entityManager != null && entityManager.isOpen()) {
        entityManager.close();
      }
    }
  }
}
