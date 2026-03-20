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
  
  @PostMapping("/register") 
  public String registerUser(@RequestParam Map<String, String> registrationInfo, Model model) {
    String userName = registrationInfo.get("userName");
    String userPassword = registrationInfo.get("userPassword");

    if (userRepository.findByUserName(userName).size() > 0) {
      model.addAttribute("errorMessage", "Username already exists! Please choose another.");
      return "redirect:/register";
    }
    userRepository.save(new User(userName, userPassword));
    return "redirect:/login";
  }

  @PostMapping("/login")
  public String login(@RequestParam Map<String, String> loginInfo, Model model, HttpServletRequest request, HttpSession session) {
    String userName = loginInfo.get("userName");
    String userPassword = loginInfo.get("userPassword");
 
    List<User> users = userRepository.findByUserNameAndUserPassword(userName, userPassword);

    if (users.isEmpty()) { 
      model.addAttribute("errorMessage", "Invalid username or password!"); 
      return "redirect:/login"; 
    }

    User user = users.get(0); 

    request.getSession().setAttribute("session_user", user);
    model.addAttribute("user", user);

    if (user.getUserRole().equals("ADMIN")) {
      return "redirect:/admin_dashboard";  
    }
    return "redirect:/dashboard";
  }

  @GetMapping("/")
  public String index(HttpServletRequest request) {
    User user = (User) request.getSession().getAttribute("session_user");
    if (user == null) return "home";
    if (user.getUserRole().equals("ADMIN")) return "redirect:/admin_dashboard";
    return "redirect:/dashboard";
  }

  @GetMapping("/login")
  public String getLogin(Model model, HttpServletRequest request, HttpSession session) {
    User user = (User) session.getAttribute("session_user");
    model.addAttribute("user", user);
    if (user != null) { return "redirect:/dashboard"; }

    return "login";
  }

  @GetMapping("/logout")
  public String destroySession(HttpServletRequest request) { 
    request.getSession().invalidate();
    return "redirect:/";
  }
 
  @GetMapping("/register") 
  public String getRegister(HttpServletRequest request, Model model) {
    User user = (User) request.getSession().getAttribute("session_user");
    model.addAttribute("user", user);
    if (user != null) { return "redirect:/logout";  }
    return "register";
  }

  @GetMapping("/dashboard")
  public String getDashboard(Model model, HttpServletRequest request) {
    User user = (User) request.getSession().getAttribute("session_user");
    if (user == null) { return "redirect:/login"; }
    if (user .getUserRole().equals("ADMIN")) { return "redirect:/admin_dashboard"; }
    model.addAttribute("user", user);
    return "users/dashboard";
  }

  // Proprietary
  @GetMapping("/admin_dashboard")
  public String getAdminDashboard(Model model, HttpServletRequest request) {
    User user = (User) request.getSession().getAttribute("session_user");
    if (user == null) return "redirect:/login";
    if (user.getUserRole().equals("ADMIN")) { 
      model.addAttribute("user", user);
      model.addAttribute("users", userRepository.findAll()); 
      return "users/admin";
    }
    return "redirect:/";
  }
}
