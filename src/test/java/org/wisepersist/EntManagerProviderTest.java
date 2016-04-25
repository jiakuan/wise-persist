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
public class EntManagerProviderTest {

  private UserDao userDao;
  private EntManagerProvider entManagerProvider;
  private final String email = "hello@acme.com";

  @BeforeMethod
  public void setUp() throws Exception {
    Injector injector = Guice.createInjector(new WisePersistModule("WisePersistUnitH2"));
    userDao = injector.getInstance(UserDao.class);

    entManagerProvider = injector.getInstance(EntManagerProvider.class);

    // save a user to DB
    User user = new User();
    user.setEmail(email);
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
    try (EntManager entManager = entManagerProvider.get()) {
      TypedQuery<User> query = entManager.em().createQuery("SELECT u FROM User u WHERE u.email=:email", User.class);
      query.setParameter("email", email);
      User user = query.getResultList().get(0);
      Assert.assertEquals(user.getEmail(), email);
    }
  }

}