package com.example.cmpt276.upsolve.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cmpt276.upsolve.models.User;
import com.example.cmpt276.upsolve.models.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @GetMapping("/")
  public String index() { 
    return "index";
  }

  @PostMapping("/login")
  public String login(@RequestParam Map<String, String> loginInfo, Model model, HttpServletRequest request, HttpSession session) {
    String userName = loginInfo.get("userName");
    String userPassword = loginInfo.get("userPassword");
    
    // System.out.println("Username: " + userName);
    // System.out.println("Password: " + userPassword);

   // Check if username exists
    List<User> usersByName = userRepository.findByUserName(userName);
    if (usersByName.isEmpty()) {
        model.addAttribute("errorMessage", "Username not found!");
        return "login";
    }
    
   // Check if username + password match
    List<User> users = userRepository.findByUserNameAndUserPassword(userName, userPassword); 
    if (users.isEmpty()) {
        model.addAttribute("errorMessage", "Incorrect password!");
        return "login";
    }

    // Successful login
    User user = users.get(0);
    request.getSession().setAttribute("session_user", user);
    model.addAttribute("user", user);

   // Role-based redirection
    if ("ADMIN".equals(user.getUserRole())) {
        model.addAttribute("users", userRepository.findAll());
        return "admin_dashboard"; 
    }
    return "dashboard";
  }

  @GetMapping("/login")
  public String getLogin(Model model, HttpServletRequest request, HttpSession session) {
    User user = (User) session.getAttribute("session_user");
    if (user == null) { return "login"; }
    model.addAttribute("user", user);
    if (user.getUserRole().equals("ADMIN")) { 
      model.addAttribute("users", userRepository.findAll());//for the show all users feature in admin_dashboard
      return "admin_dashboard"; 
    }
    return "dashboard";
  }

  @GetMapping("/logout")
  public String destroySession(HttpServletRequest request) { 
    request.getSession().invalidate();
    return "redirect:/login";
  }
  
  @GetMapping("/register") 
  public String register() {
    return "register";

  }

  @PostMapping("/register") 
  public String registerUser(@RequestParam Map<String, String> registrationInfo, Model model) {
    String userName = registrationInfo.get("userName");
    String userPassword = registrationInfo.get("userPassword");
    if (userRepository.findByUserName(userName).size() > 0) {
      model.addAttribute("errorMessage", "Username already exists! Please choose another.");
      return "register";
    }
    userRepository.save(new User(userName, userPassword));
    return "redirect:/login";
  }

  @GetMapping("/dashboard")
  public String getCreateCard(Model model, HttpServletRequest request) {
      User user = (User) request.getSession().getAttribute("session_user");
      if (user == null) { return "redirect:/login"; }
      model.addAttribute("user", user);
      return "dashboard";
  }

  @GetMapping("/admin_dashboard")
  public String getCreateCardAdmin(Model model, HttpServletRequest request) {
      User user = (User) request.getSession().getAttribute("session_user");
      if (user == null) { return "redirect:/login"; }
      model.addAttribute("user", user);
      model.addAttribute("users", userRepository.findAll());
      return "admin_dashboard";
  }

}
