/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author jiakuanwang
 */
@Singleton
public class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {

  private static final Logger log = LoggerFactory.getLogger(EntityManagerFactoryProvider.class);

  private final String persistUnit;

  @Inject
  public EntityManagerFactoryProvider(@PersistUnit String persistUnit) {
    this.persistUnit = persistUnit;
  }

  @Override
  public EntityManagerFactory get() {
    final EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistUnit);
    log.info("Initialized entity manager factory.");
    return emf;
  }
}
