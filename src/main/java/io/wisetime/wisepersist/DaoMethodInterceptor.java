/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author jiakuanwang
 */
public abstract class DaoMethodInterceptor implements MethodInterceptor {

  private final EntityManagerFactory emf;

  public DaoMethodInterceptor(String persistUnit) {
    emf = EntityManagerFactoryProvider.get(persistUnit);
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    EntityManager entityManager = emf.createEntityManager();
    Object dao = invocation.getThis();
    if (!AbstractDao.class.isAssignableFrom(dao.getClass())) {
      throw new IllegalStateException(
          dao.getClass().getName() + " must extend " + AbstractDao.class.getName());
    }

    Method setMethod = dao.getClass().getMethod("setEntityManager", EntityManager.class);
    setMethod.invoke(dao, entityManager);

    Object result = invokeWithEntityManager(invocation, entityManager);
    setMethod.invoke(dao, (EntityManager) null);
    return result;
  }

  protected abstract Object invokeWithEntityManager(
      MethodInvocation invocation, EntityManager entityManager) throws Throwable;
}
