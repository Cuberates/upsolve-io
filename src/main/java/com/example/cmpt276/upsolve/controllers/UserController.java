package com.example.cmpt276.upsolve.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cmpt276.upsolve.models.User;
import com.example.cmpt276.upsolve.models.UserRepository;

@Controller
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @PostMapping("/login")
  public String loginUser(@RequestParam Map<String, String> loginInfo) {
    String userName = loginInfo.get("userName");
    String userPassword = loginInfo.get("userPassword");

    System.out.println("Username: " + userName);
    System.out.println("Password: " + userPassword);

    if (userRepository.findByUserNameAndUserPassword(userName, userPassword).size() == 0) {
      return "Invalid username or password";
    }
    return "dashboard";
  }
  @PostMapping("/register")
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
