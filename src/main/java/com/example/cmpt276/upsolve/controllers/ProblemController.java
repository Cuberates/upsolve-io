package com.example.cmpt276.upsolve.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cmpt276.upsolve.models.Problem;
import com.example.cmpt276.upsolve.models.ProblemRepository;

@Controller
public class ProblemController {
  @Autowired
  private ProblemRepository problemRepository;

  @PostMapping("/create_card")
  public String createCard(@RequestParam Map<String, String> cardInfo) {
    String problemName = cardInfo.get("problemName");
    String problemDescription = cardInfo.get("problemDescription");
    String problemSolution = cardInfo.get("problemSolution");
    int problemDifficulty = Integer.parseInt(cardInfo.get("problemDifficulty"));

    if (problemRepository.findByProblemName(problemName).size() > 0) {
      return "Problem already exists";
    }

    problemRepository.save(new Problem(problemName, problemDescription, problemSolution, problemDifficulty));
    
    return "dashboard";
  }

}