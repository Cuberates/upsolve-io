package com.example.cmpt276.upsolve.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  List<User> findByUserNameAndUserPassword(String userName, String userPassword);
  List<User> findByUserID(int userID);
  List<User> findByUserName(String userName); 
} 