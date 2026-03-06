package com.example.cmpt276.upsolve.models.User;

import jakarta.persistence.Id;

public class User {
  @Id 
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
