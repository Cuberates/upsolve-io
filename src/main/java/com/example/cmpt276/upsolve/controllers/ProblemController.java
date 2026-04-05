package com.example.cmpt276.upsolve.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @GetMapping("/problems")
  public String getProblems(Model viewModel, HttpServletRequest request) {  
    User user = (User) request.getSession().getAttribute("session_user");
    if (user == null) { return "redirect:/login"; }
    
    viewModel.addAttribute("user", user);
    List<Problem> problems;
    if (user.getUserRole().equals("ADMIN")) {
        problems = problemRepository.findAll();
    } else {
        problems = problemRepository.findByUserID(user.getUserID());
    }

    for (Problem problem : problems) {
        int total = problem.getCorrectAttempts() + problem.getIncorrectAttempts();
        double accuracy = (total == 0) ? -1 : (problem.getCorrectAttempts() * 100.0 / total);
        problem.setAccuracy(accuracy); // you’ll need a transient field or setter in Problem.java
    }

    viewModel.addAttribute("problems", problems);
    
    return "cards/view_all";
  }

  @GetMapping("/problems/view/{problemID}")
  public String getViewCard(@PathVariable("problemID") int problemID, HttpServletRequest request, Model viewModel) {
    User user = (User) request.getSession().getAttribute("session_user");
    if (user == null) { return "redirect:/login"; }
    
    Problem problem = problemRepository.findByProblemID(problemID).get(0); 
    if (problem == null) { return "redirect:/error"; } 
    if (user.getUserRole().equals("USER") && !problem.getUserID().equals(user.getUserID())) {
      return "redirect:/problems";
    }
    problem.setStudied(true);
    problemRepository.save(problem);
    
    viewModel.addAttribute("user", user);
    viewModel.addAttribute("problem", problem);
    return "cards/study";
  }

  @GetMapping("/problems/new")
  public String getCreateCard(Model viewModel, HttpServletRequest request) {
    User user = (User) request.getSession().getAttribute("session_user");
    if (user == null) { return "redirect:/login"; }
    
    viewModel.addAttribute("user", user);
    return "cards/create";
  }

  @GetMapping("/problems/update/{problemID}") 
  public String getUpdateCard(@PathVariable("problemID") int problemID, HttpServletRequest request, Model viewModel) {
    User user = (User) request.getSession().getAttribute("session_user");
    if (user == null) { return "redirect:/login"; }
    
    Problem problem = problemRepository.findByProblemID(problemID).get(0);
    if (problem == null) { return "redirect:/error"; } 

    if (user.getUserRole().equals("USER") && (problem.getUserID() == null || !problem.getUserID().equals(user.getUserID()))) {
      return "redirect:/problems";
    }

    viewModel.addAttribute("user", user);
    viewModel.addAttribute("problem", problem);
    return "cards/update";
  }
 
  @PostMapping("/problems/new")
  public String createCard(@RequestParam Map<String, String> problemInfo, HttpServletRequest request, Model viewModel) { 
    User user = (User) request.getSession().getAttribute("session_user");
    if (user == null) {
        return "redirect:/login";
    }
   
    String problemName = problemInfo.get("problemName");
    String problemDescription = problemInfo.get("problemDescription");
    String problemSolution = problemInfo.get("problemSolution");
    String problemType = problemInfo.get("problemType");
    int problemDifficulty = Integer.parseInt(problemInfo.get("problemDifficulty"));

    if (problemRepository.findByProblemName(problemName).size() > 0) {
      viewModel.addAttribute("user", user);
      viewModel.addAttribute("errorMessage", "Problem already exists!");
      return "cards/create";
    }
    Problem newProblem = new Problem(problemName, problemDescription, problemSolution, problemDifficulty, problemType);
    newProblem.setUserID(user.getUserID());
    problemRepository.save(newProblem);
    if (user.getUserRole().equals("ADMIN")) {
      return "redirect:/admin_dashboard";
    }
    return "redirect:/dashboard";  
  }

  @PostMapping("/problems/delete/{problemID}")
  public String deleteCard(@PathVariable("problemID") int problemID, HttpServletRequest request) {
    System.out.println("PROBLEM ID: " + problemID);
    User user = (User) request.getSession().getAttribute("session_user");
    if (user == null) {
        return "redirect:/login";
    } 
    Problem problem = problemRepository.findByProblemID(problemID).get(0);
    if (problem == null) { 
      return "redirect:/error"; // This is not supposed to happen since we are not querying for the problem ID;
    }

    if (user.getUserRole().equals("USER") && (problem.getUserID() == null || !problem.getUserID().equals(user.getUserID()))) {
      return "redirect:/problems";
    }
    
    problemRepository.delete(problem);
    if (user.getUserRole().equals("ADMIN")) {
      return "redirect:/problems";
    }
    return "redirect:/problems";  
  }

  @GetMapping("/problems/delete/{problemID}")
  public String deleteCardGet(@PathVariable("problemID") int problemID, HttpServletRequest request) {
    User user = (User) request.getSession().getAttribute("session_user");
    if (user == null) {
        return "redirect:/login";
    } 
    return deleteCard(problemID, request);
  }

  @PostMapping("/problems/update/{problemID}")
  public String updateCard(@PathVariable("problemID") int problemID, @RequestParam Map<String, String> problemInfo, HttpServletRequest request) {
    User user = (User) request.getSession().getAttribute("session_user");
    if ( user == null) { return "redirect:/login"; }
    Problem problem = problemRepository.findByProblemID(problemID).get(0);
    if (problem == null) { 
      return "redirect:/error"; // This is not supposed to happen since we are not querying for the problem ID;
    }

    if (user.getUserRole().equals("USER") && (problem.getUserID() == null || !problem.getUserID().equals(user.getUserID()))) {
      return "redirect:/problems";
    }
    problem.setProblemName(problemInfo.get("problemName"));
    problem.setProblemDescription(problemInfo.get("problemDescription"));
    problem.setProblemSolution(problemInfo.get("problemSolution"));
    problem.setProblemDifficulty(Integer.parseInt(problemInfo.get("problemDifficulty")));
    problem.setProblemType(problemInfo.get("problemType"));
    problemRepository.save(problem);
    if (user.getUserRole().equals("ADMIN")) {
      return "redirect:/admin_dashboard";
    }
    return "redirect:/dashboard";   
  }

  @PostMapping("/problems/{problemID}/attempt")
  public String recordAttempt(@PathVariable("problemID") int problemID, @RequestParam("result") String result, HttpServletRequest request) {
    User user = (User) request.getSession().getAttribute("session_user");
    if (user == null) return "redirect:/login";

    Problem problem = problemRepository.findByProblemID(problemID).get(0);
    if (problem == null) return "redirect:/error";

    if (result.equals("correct")) {
      problem.setCorrectAttempts(problem.getCorrectAttempts() + 1);
    } else if (result.equals("incorrect")) {
      problem.setIncorrectAttempts(problem.getIncorrectAttempts() + 1);
    }

    problemRepository.save(problem);

    return "redirect:/problems/view/" + problemID;
}

  @PostMapping("/problems/update/{problemID}/difficulty")
  public String updateDifficulty(@PathVariable("problemID") int problemID, @RequestParam("problemDifficulty") int problemDifficulty, HttpServletRequest request) {
    User user = (User) request.getSession().getAttribute("session_user");
    if (user == null) { return "redirect:/login"; }

    Problem problem = problemRepository.findByProblemID(problemID).get(0);
    if (problem == null) { 
      return "redirect:/error"; 
    }

    if (user.getUserRole().equals("USER") && (problem.getUserID() == null || !problem.getUserID().equals(user.getUserID()))) {
      return "redirect:/problems";
    }
    
    problem.setProblemDifficulty(problemDifficulty);
    problemRepository.save(problem);

    return "redirect:/problems/view/" + problemID;
  }

}