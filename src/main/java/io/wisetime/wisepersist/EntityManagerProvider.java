/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author jiakuanwang
 */
public class EntityManagerProvider implements Provider<EntityManager> {

  private static final Logger log = LoggerFactory.getLogger(EntityManagerProvider.class);

  @Inject
  private EntityManagerFactory emf;

  @Override
  public EntityManager get() {
    log.debug("Creating a new entity manager...");
    return emf.createEntityManager();
  }
}
