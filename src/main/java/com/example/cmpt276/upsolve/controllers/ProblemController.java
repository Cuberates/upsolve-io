package com.example.cmpt276.upsolve.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cmpt276.upsolve.models.Problem;
import com.example.cmpt276.upsolve.models.ProblemRepository;
import com.example.cmpt276.upsolve.models.User;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ProblemController {
  @Autowired
  private ProblemRepository problemRepository;

  @GetMapping("/create_card")
  public String getCreateCard(Model model, HttpServletRequest request) {
      User user = (User) request.getSession().getAttribute("session_user");
      if (user == null) { return "redirect:/login"; }
      model.addAttribute("user", user);
      return "create_card";
  }

  @PostMapping("/create_card")
  public String createCard(@RequestParam Map<String, String> cardInfo, HttpServletRequest request) {
    String problemName = cardInfo.get("problemName");
    String problemDescription = cardInfo.get("problemDescription");
    String problemSolution = cardInfo.get("problemSolution");
    int problemDifficulty = Integer.parseInt(cardInfo.get("problemDifficulty"));

    if (problemRepository.findByProblemName(problemName).size() > 0) {
      return "Problem already exists";
    }

    problemRepository.save(new Problem(problemName, problemDescription, problemSolution, problemDifficulty));

    User user = (User) request.getSession().getAttribute("session_user");
    if (user != null && user.getUserRole().equals("ADMIN")) {
        return "redirect:/admin_dashboard";
    }
    return "dashboard";
  }

}