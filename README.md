## WisePersist

WisePersist is a simple JPA wrapper which provides `@Transactional` and `@NonTransactional` annotations and pre-configured
Guice module.


### Configure Maven dependency

WisePersist is available in Maven central repository: 

http://search.maven.org/#search%7Cga%7C1%7Cwise-persist

To add the dependency to your `build.gradle` (for Gradle projects) or `pom.xml` (for Maven projects), use the following configuration:

For Gradle projects:

```
compile 'org.wisepersist:wise-persist:1.0.2'
```

For Maven projects:

```
<dependency>
    <groupId>org.wisepersist</groupId>
    <artifactId>wise-persist</artifactId>
    <version>1.0.2</version>
</dependency>
```

If you would like to use the 1.0.3-SNAPSHOT release, use this configuration.

```
<dependency>
    <groupId>org.wisepersist</groupId>
    <artifactId>wise-persist</artifactId>
    <version>1.0.3-SNAPSHOT</version>
</dependency>
```

In order to use snapshot releases you also need to add the Sonatype snapshots repository to your POM:

```
<repositories>
    <repository>
        <id>sonatype-nexus-snapshots</id>
        <url>http://oss.sonatype.org/content/repositories/snapshots</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
        <releases>
            <enabled>false</enabled>
        </releases>
    </repository>
</repositories>
```

### How to use WisePersist?

In your JPA projects, mark any public methods which are expected to be transactional with `@Transactional` annotation or `@NonTransactional` for non transactional methods. Please note that all public methods in your DAO classes must be annotated by either `@Transactional` or `@NonTransactional`, or you will get exception mentioning this.

For example:

```
import com.google.common.base.Optional;

import java.util.List;

import org.wisepersist.AbstractDao;
import org.wisepersist.NonTransactional;
import org.wisepersist.Transactional;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * @author jiakuanwang
 */
public class UserDao extends AbstractDao {

  @Transactional
  public User saveUser(User user) {
    return em().merge(user);
  }

  @NonTransactional
  public User Optional<User> findById(Long userId) {
    TypedQuery<DataUser> query =
        em().createQuery("SELECT u FROM User u WHERE u=:userId", DataUser.class);
    query.setParameter("userId", userId);

    List<User> userList = query.getResultList();
    if (userList != null && userList.size() > 0) {
	  User user = userList.get(0);
      return Optional.of(user);
    }
    return Optional.absent();
  }
}
```

In the example above, we need to extend the AbstractDao class for every DAO class firstly.

In public DAO methods, we could just use `em()` to get the current available entity manager instance.

Each method with `@Transactional` annotation will start a new transaction and commit/rollback the transaction automatically. Please note that transactional methods cannot be nested. That is, in the example above, inside the `saveUser` method mentioned above should not call any other methods annotated with `@Transactional` or `@NonTransactional`.

After that, you can use this DAO with Guice injector. For example:

```
/**
 * @author jiakuanwang
 */
public class UserDaoTest {

  private final Injector injector = Guice.createInjector(
      new WisePersistModule("WTPersistUnitH2")
  );
  private final UserDao userDao = injector.getInstance(UserDao.class);

  @Test
  public void testSaveUser() throws Exception {
    User user = new User();
    user.setEmail("delight.wjk@gmail.com");
    user.setFirstName("Jake");
    user.setLastName("Wang");
    userDao.saveUser(user);
  }
}
```

As shown in the code above, the first step is to create `WisePersistModule` with persist unit name (or you could pass a custom entity manager factory) and put in the Guice module list when creating the Guice injector, and then get a `UserDao` instance from the Guice injector, now you are ready to go!

With this simple tiny framework, we don't need to manually begin and close transactions again and again.
