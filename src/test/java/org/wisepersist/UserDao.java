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
