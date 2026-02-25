package com.example.cmpt276.upsolve.models.Problem;
import java.util.List;

public interface ProblemRepository {
  List<Problem> findByProblemId(Integer problemId);
  // problemTag is compressed from all tags in the problemTags list, separated by comma.
  List<Problem> findByProblemTagList(String problemTag);
  List<Problem> findByProblemName(String problemName);
  List<Problem> findByProblemStatement(String problemStatement);
  List<Problem> findAll();
  void save(Problem problem);
}