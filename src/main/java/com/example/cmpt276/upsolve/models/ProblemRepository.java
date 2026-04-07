package com.example.cmpt276.upsolve.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProblemRepository extends JpaRepository<Problem, Integer> {
  List<Problem> findByProblemName(String problemName);
  List<Problem> findByProblemID(Integer problemID);
  @Query("SELECT p FROM Problem p WHERE p.userID = :userID ORDER BY p.problemID ASC")
  List<Problem> findByUserID(@Param("userID") Integer userID);
}  
