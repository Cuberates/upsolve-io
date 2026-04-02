package com.example.cmpt276.upsolve.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.cmpt276.upsolve.models.User;
import com.example.cmpt276.upsolve.models.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
  String hashPassword(String password) {
    // Use Cesart cipher for password hashing (not secure, but sufficient for demonstration purposes)
    StringBuilder hashed = new StringBuilder();
    for (char c : password.toCharArray()) {
      hashed.append((char) ((c + 3) % 256)); // Shift characters by 3 positions
    }
    return hashed.toString();
  }
  @Autowired
  private UserRepository userRepository;
  
  @PostMapping("/register") 
  public String registerUser(@RequestParam Map<String, String> registrationInfo, RedirectAttributes redirectAttributes, Model model) {
    String userName = registrationInfo.get("userName");
    String userPassword = registrationInfo.get("userPassword");
    String userEmail = registrationInfo.get("userEmail");

    String confirmedUserEmail = registrationInfo.get("confirmedUserEmail");
    String confirmedUserPassword = registrationInfo.get("confirmedUserPassword");

    // Handling invalid registration info
    if (userName == null) { return "redirect:/register"; }   
    if (userRepository.findByUserName(userName).size() > 0) {
      redirectAttributes.addFlashAttribute("errorMessage", "Username already exists! Please choose another.");
      return "redirect:/register";
    }

    if (userEmail == null) { return "redirect:/register"; }
    if (!userEmail.equals(confirmedUserEmail)) {
      redirectAttributes.addFlashAttribute("errorMessage", "Emails do not match! Please try again");
      return "redirect:/register";
    }

    if (userPassword == null) { return "redirect:/register"; }
    if (!userPassword.equals(confirmedUserPassword)) {
      redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match! Please try again");
      return "redirect:/register";
    }
    userRepository.save(new User(userName, userEmail, hashPassword(userPassword)));
    return "redirect:/login";
  }

  @PostMapping("/login")
  public String login(@RequestParam Map<String, String> loginInfo, RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpSession session) {
    String userName = loginInfo.get("userName");
    String userPassword = loginInfo.get("userPassword");

    List<User> users = userRepository.findByUserNameAndUserPassword(userName, hashPassword(userPassword));

    if (users.isEmpty()) { 
      redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password!"); 
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
