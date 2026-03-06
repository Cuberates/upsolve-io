package com.example.cmpt276.upsolve.models.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository{
  List<User> findByUserNameAndPassword(String userName, String userPassword);
  List<User> findByUserName(String userName); 
} 