package com.example.cmpt276.upsolve.controllers;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cmpt276.upsolve.models.Problem.Problem;
import com.example.cmpt276.upsolve.models.Problem.ProblemRepository;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ProblemController {
  private static final String PROBLEM_LIST_PATH = "/problems";
  private static final String PROBLEM_VIEW_PATH = "/view/problems/{problemId}";
  private static final String PROBLEM_CREATE_PATH = "/problems/create";
  private static final String PROBLEM_EDIT_PATH = "/problems/edit/{problemId}";
  private static final String PROBLEM_DELETE_PATH = "/problems/delete/{problemId}";
    
  @Autowired
  private ProblemRepository problemRepository;

  @GetMapping(PROBLEM_VIEW_PATH)
  public String getProblemById(@PathVariable Integer problemId, Model model, HttpServletResponse response) { 
    Problem problem = problemRepository.findByProblemId(problemId).get(0);
    if (problem == null) {
      response.setStatus(400);
      return "redirect:/problems";
    }
    model.addAttribute("problem", problem); 
    return "problem";
  }

  @GetMapping(PROBLEM_LIST_PATH)
  public String getAllProblems(Model model) {
    List<Problem> problems = problemRepository.findAll();
    model.addAttribute("problems", problems);
    return "problems";
  }
  
  @GetMapping(PROBLEM_CREATE_PATH)
  public String createProblem(@RequestParam Map<String, String> newProblem, HttpServletResponse response) {
    String problemStatement = newProblem.get("problemStatement");
    String problemName = newProblem.get("problemName");
    List<String> problemTags = List.of(newProblem.get("problemTags").split(","));

    Problem problem = new Problem(problemName, problemStatement, problemTags);

    problemRepository.save(problem);
    return "redirect:/problems"; 
  }

  @PostMapping(PROBLEM_EDIT_PATH)
  public String editProblem(@PathVariable Integer problemId, @RequestParam Map<String, String> updatedProblem, HttpServletResponse response) {
    return null;
  }

  @PostMapping(PROBLEM_DELETE_PATH) 
  public String deleteProblem(@PathVariable Integer problemId, HttpServletResponse response) {
    return null;
  }
  
}


