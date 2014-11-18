/*
 * Copyright (c) 2014 WisePersist
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wisepersist;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

/**
 * @author jiakuanwang
 */
public class EntityManagerFactoryProvider {

  private static final Logger log = LoggerFactory.getLogger(EntityManagerFactoryProvider.class);

  private static final Map<String, EntityManagerFactory> cache = Maps.newHashMap();

  public static EntityManagerFactory get(String persistUnit) {
    return get(persistUnit, null, null);
  }

  /**
   * Creates a new entity manager factory if it doesn't exist in cache.
   *
   * @param persistUnit          persist unit name configured in
   *                             a <code>persistence.xml</code> file.
   * @param dsProvider           custom data source provider, could be null.
   *                             See {@link org.wisepersist.DataSourceProvider}
   * @param additionalProperties additional properties for the entity manager
   *                             factory we are creating.
   * @return the entity manager factory.
   */
  public static EntityManagerFactory get(String persistUnit,
                                         DataSourceProvider dsProvider,
                                         Map<String, Object> additionalProperties) {
    Preconditions.checkArgument(
        !Strings.isNullOrEmpty(persistUnit), "persistUnit cannot be null or empty");

    if (additionalProperties == null) {
      additionalProperties = Maps.newHashMap();
    }

    EntityManagerFactory emf = cache.get(persistUnit);
    if (emf == null) {
      try {
        emf = Persistence.createEntityManagerFactory(persistUnit, additionalProperties);
        if (dsProvider != null) {
          EntityManager em = emf.createEntityManager();

          Map<String, Object> properties = emf.getProperties();

          String jdbcUrl = (String) properties.get("javax.persistence.jdbc.url");
          String jdbcDriver = (String) properties.get("javax.persistence.jdbc.driver");
          String jdbcUser = (String) properties.get("javax.persistence.jdbc.user");
          String jdbcPass = (String) properties.get("javax.persistence.jdbc.password");

          em.close();
          emf.close();

          DataSource dataSource = dsProvider.get(jdbcUrl, jdbcDriver, jdbcUser, jdbcPass);
          additionalProperties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, dataSource);
          emf = Persistence.createEntityManagerFactory(persistUnit, additionalProperties);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        throw new DaoException("Failed to create entity manager factory: " + e.getMessage(), e);
      }
      cache.put(persistUnit, emf);
      log.info("Initialized entity manager factory for persist unit '" + persistUnit + "'.");
    }
    return emf;
  }
}
