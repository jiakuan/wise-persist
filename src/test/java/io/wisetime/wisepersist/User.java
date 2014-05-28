/*
 * Copyright (c) 2014 WiseTime Pty Ltd. All rights reserved.
 */

package io.wisetime.wisepersist;

import com.google.common.base.Objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author jiakuanwang
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "user_key"})
})
public class User extends BaseEntity<User> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("id", id)
        .add("email", email)
        .add("firstName", firstName)
        .add("lastName", lastName)
        .toString();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  protected Object[] getHashCodeData() {
    return new Object[]{getEmail(), getEmail()};
  }

  @Override
  protected User getThis() {
    return this;
  }

  @Override
  public Serializable getPk() {
    return id;
  }

  @Override
  protected boolean dataEquals(User other) {
    return areEqual(getEmail(), other.getEmail());
  }
}
