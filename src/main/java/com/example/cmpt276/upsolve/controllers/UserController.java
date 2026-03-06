package com.example.cmpt276.upsolve.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cmpt276.upsolve.models.User.User;
import com.example.cmpt276.upsolve.models.User.UserRepository;

import jakarta.persistence.Table;

@Table(name = "users")
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @GetMapping("/users/login")
  public String loginUser(@RequestParam Map<String, String> loginInfo) {
    String userName = loginInfo.get("userName");
    String userPassword = loginInfo.get("userPassword");
    if (userRepository.findByUserNameAndPassword(userName, userPassword).isEmpty()) {
      return "Invalid username or password";
    }
    return "dashboard";
  }
  @PostMapping("/users/register")
  public String registerUser(@RequestParam Map<String, String> registrationInfo) {
    String userName = registrationInfo.get("userName");
    String userPassword = registrationInfo.get("userPassword");
    if (userRepository.findByUserName(userName).size() > 0) {
      return "Username already exists";
    }
    userRepository.save(new User(userName, userPassword));
    return "dashboard";
  }
}
