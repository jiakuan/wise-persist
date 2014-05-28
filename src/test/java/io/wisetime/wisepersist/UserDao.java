/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import com.google.common.base.Optional;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * @author jiakuanwang
 */
public class UserDao extends AbstractDao {

  @Transactional
  public User saveUser(User user) {
    return getEntityManager().merge(user);
  }

  @NonTransactional
  public Optional<User> findByEmail(String email) {
    TypedQuery<User> query = getEntityManager().createQuery(
        "SELECT user FROM User user WHERE user.email=:email", User.class
    );
    query.setParameter("email", email);
    List<User> users = query.getResultList();
    if (users.size() > 0) {
      return Optional.of(users.get(0));
    }
    return Optional.absent();
  }

  /**
   * Dangerous method for unit test only.
   */
  @Transactional
  public synchronized int deleteAll() {
    Query query = getEntityManager().createNativeQuery("DELETE FROM users");
    return query.executeUpdate();
  }
}
