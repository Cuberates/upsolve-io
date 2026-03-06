package com.example.cmpt276.upsolve.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Table(name="users")
@Entity
public class User {
  @Id 
  @GeneratedValue
  Integer userID;
  String userName; 
  String userPassword;

  public User() {}
  public User(String userName, String userPassword) {
    this.userName = userName;
    this.userPassword = userPassword; 
  }
  public Integer getUserID() {
    return userID;
  }
  public void setUserID(Integer userID) {
    this.userID = userID;
  }
  public String getUserName() {
    return userName;
  }  
  public void setUserName(String userName) {
    this.userName = userName;  
  }
  public String getUserPassword() {
    return userPassword;
  }
  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }
} 
