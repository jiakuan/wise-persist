/*
 * Copyright (c) 2016 WisePersist
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

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.TypedQuery;

/**
 * @author thomas.haines.
 */
public class AutoCloseableEntityManagerProviderTest {

  private static final String TEST_EMAIL = "hello@acme.com";

  private UserDao userDao;
  private AutoCloseableEntityManagerProvider entManagerProvider;

  @BeforeMethod
  public void setUp() throws Exception {
    Injector injector = Guice.createInjector(new WisePersistModule("WisePersistUnitH2"));
    userDao = injector.getInstance(UserDao.class);

    entManagerProvider = injector.getInstance(AutoCloseableEntityManagerProvider.class);

    // save a user to DB
    User user = new User();
    user.setEmail(TEST_EMAIL);
    user.setFirstName("Joe");
    user.setLastName("Bloggs");
    userDao.saveUser(user);
  }

  @AfterMethod
  public void tearDown() throws Exception {
    userDao.deleteAll();
  }

  @Test
  public void testProvider() throws Exception {
    try (AutoCloseableEntityManager entManager = entManagerProvider.get()) {
      TypedQuery<User> query =
          entManager.em().createQuery("SELECT u FROM User u WHERE u.email=:email", User.class);
      query.setParameter("email", TEST_EMAIL);
      User user = query.getResultList().get(0);
      Assert.assertEquals(user.getEmail(), TEST_EMAIL);
    }
  }
}
