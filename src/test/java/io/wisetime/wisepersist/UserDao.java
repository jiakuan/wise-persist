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
    return em().merge(user);
  }

  @NonTransactional
  public Optional<User> findByEmail(String email) {
    TypedQuery<User> query = em().createQuery(
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
   * This is a wrong method which should throw exception.
   */
  @Transactional
  public User nestedFindByEmail(String email) {
    Optional<User> userOpt = findByEmail(email);
    return userOpt.isPresent() ? userOpt.get() : null;
  }

  /**
   * Dangerous method for unit test only.
   */
  @Transactional
  protected synchronized int deleteAll() {
    Query query = em().createNativeQuery("DELETE FROM users");
    return query.executeUpdate();
  }
}
