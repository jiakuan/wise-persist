/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author jiakuanwang
 */
public class EntityManagerFactoryProvider {

  private static final Logger log = LoggerFactory.getLogger(EntityManagerFactoryProvider.class);

  private static EntityManagerFactory emf;

  public static EntityManagerFactory get(String persistUnit) {
    if (emf == null) {
      emf = Persistence.createEntityManagerFactory(persistUnit);
      log.info("Initialized entity manager factory.");
    }
    return emf;
  }
}
