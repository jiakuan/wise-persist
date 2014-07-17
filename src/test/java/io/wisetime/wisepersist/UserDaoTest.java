/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import com.google.common.base.Optional;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * @author jiakuanwang
 */
public class UserDaoTest {

  private UserDao userDao;

  @BeforeClass
  public void beforeClass() throws Exception {
    Injector injector = Guice.createInjector(new WisePersistModule("WisePersistUnitH2"));
    userDao = injector.getInstance(UserDao.class);
  }

  @AfterMethod
  public void afterMethod() throws Exception {
    userDao.deleteAll();
  }

  @Test
  public void testSaveUser() throws Exception {
    User user = createUser();
    User saved = userDao.saveUser(user);
    assertNotNull(saved);
    assertNotNull(saved.getId());
    Assert.assertEquals(saved.getEmail(), user.getEmail());
  }

  @Test
  public void testFindByEmail() throws Exception {
    User user = createUser();
    userDao.saveUser(user);

    Optional<User> retrieved = userDao.findByEmail(user.getEmail());
    Assert.assertTrue(retrieved.isPresent());
    Assert.assertEquals(retrieved.get().getEmail(), user.getEmail());
    Assert.assertEquals(retrieved.get().getFirstName(), user.getFirstName());
    Assert.assertEquals(retrieved.get().getLastName(), user.getLastName());
  }

  @Test(expectedExceptions = DaoException.class)
  public void testNestedFindByEmail() {
    User user = createUser();
    userDao.saveUser(user);

    userDao.nestedFindByEmail(user.getEmail());
  }

  @Test(expectedExceptions = DaoException.class)
  public void testInitialization_WithException() {
    Guice.createInjector(
        new WisePersistModule("WisePersistUnitH2"),
        new WisePersistModule("WisePersistUnitH2")
    );
  }

  @Test
  public void testInitialization_WithoutException() {
    assertNotNull(Guice.createInjector(new WisePersistModule("WisePersistUnitH2")));
    assertNotNull(Guice.createInjector(new WisePersistModule("WisePersistUnitH2")));
  }

  private User createUser() {
    User user = new User();
    user.setEmail("delight.wjk@gmail.com");
    user.setFirstName("Jake");
    user.setLastName("Wang");
    return user;
  }
}
