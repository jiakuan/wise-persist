/*
 * Copyright 2014 WiseTime Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.wisetime.wisepersist;

import com.google.common.base.Preconditions;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

/**
 * @author jiakuanwang
 */
public class TransactionalInterceptor implements MethodInterceptor {

  private static Logger log = LoggerFactory.getLogger(TransactionalInterceptor.class);

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    // Check arguments - the first argument must be EntityManager
    final Object[] arguments = invocation.getArguments();
    Preconditions.checkNotNull(arguments);
    Preconditions.checkState(arguments.length > 0);
    Preconditions.checkState(arguments[0] instanceof EntityManager);

    EntityManager em = (EntityManager) arguments[0];
    try {
      em.getTransaction().begin();
      Object result = invocation.proceed();
      em.getTransaction().commit();

      return result;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      em.getTransaction().rollback();
      throw e;
    } finally {
      if (em != null && em.isOpen()) {
        em.close();
      }
    }
  }
}
