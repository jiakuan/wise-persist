## WisePersist

WisePersist is simple JPA wrapper which provides @Transactional annotation and pre-configured Guice
module.


### Configure Maven repository

Firstly, add the Maven repository to your projects:

In Gradle projects, use:

```
repositories {
    maven {
        url "https://dl.dropboxusercontent.com/u/13110611/mvn-repo"
    }
}
```

In Maven projects, use:

```
<repositories>
    <repository>
        <id>jake-dropbox</id>
        <name>jake-dropbox</name>
        <url>https://dl.dropboxusercontent.com/u/13110611/mvn-repo</url>
    </repository>
</repositories>
```

### Add Maven dependency

And then, add dependency to your build.gradle (for Gradle projects) or pom.xml (for Maven
projects).

In Gradle projects, use:

```
compile 'io.wisetime:wise-persist:1.0.0'
```

In Maven projects, use:

```
<dependency>
    <groupId>io.wisetime</groupId>
    <artifactId>wise-persist</artifactId>
    <version>1.0.0</version>
</dependency>
```

### How to use WisePersist?

In your JPA projects, mark any methods which are expected as transactional with @Transactional
annotation. For example:

```
/**
 * @author jiakuanwang
 */
public class UserDao {

  @Transactional
  public User saveUser(EntityManager em, User user) {
    return em.merge(user);
  }
}
```

Each method with @Transactional annotation will start a new transaction and commit/rollback the
transaction automatically. Please note that transactional methods cannot be nested,
e.g. inside the `saveUser` method mentioned above should not call any other methods annotated
with @Transactional.

After that, you can use this DAO with Guice injector. For example:

```
/**
 * @author jiakuanwang
 */
public class UserDaoTest {

  private final Injector injector = Guice.createInjector(new WisePersistModule("WTPersistUnitH2"));
  private final EntityManagerFactory emf = injector.getInstance(EntityManagerFactory.class);
  private final UserDao userDao = injector.getInstance(UserDao.class);

  @Test
  public void testSaveUser() throws Exception {
    User user = new User();
    user.setEmail("jake@wisetime.io");
    user.setFirstName("Jake");
    user.setLastName("Wang");
    userDao.saveUser(emf.createEntityManager(), user);
  }
}
```

Add `WisePersistModule` in your Guice injector module list, get `EntityManagerFactory` (it's a
singleton instance) from the Guice instance, and also get a `UserDao` instance from the Guice
injector, now you are ready to go!

With this simple tiny framework, we don't need to manually begin and close transactions again and
 again.

