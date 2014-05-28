/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import com.google.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author jiakuanwang
 */
public abstract class DaoMethodInterceptor implements MethodInterceptor {

  @Inject
  private EntityManagerFactory emf;

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

    return invokeWithEntityManager(invocation, entityManager);
  }

  protected abstract Object invokeWithEntityManager(
      MethodInvocation invocation, EntityManager entityManager) throws Throwable;
}
