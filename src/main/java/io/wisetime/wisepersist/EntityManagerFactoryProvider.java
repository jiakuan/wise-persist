/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author jiakuanwang
 */
public class EntityManagerFactoryProvider {

  private static final Logger log = LoggerFactory.getLogger(EntityManagerFactoryProvider.class);

  private static final Map<String, EntityManagerFactory> cache = Maps.newHashMap();

  public static EntityManagerFactory get(String persistUnit) {
    EntityManagerFactory emf = cache.get(persistUnit);
    if (emf == null) {
      emf = Persistence.createEntityManagerFactory(persistUnit);
      cache.put(persistUnit, emf);
      log.info("Initialized entity manager factory for persist unit '" + persistUnit + "'.");
    }
    return emf;
  }
}
