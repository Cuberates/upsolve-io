package com.example.cmpt276.upsolve.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblamRepository extends JpaRepository<Problem, Integer> {
  List<Problem> findByProblemName(String problemName);
} 
