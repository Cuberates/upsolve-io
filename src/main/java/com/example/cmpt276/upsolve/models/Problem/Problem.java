package com.example.cmpt276.upsolve.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*TODO: 
- Design complete UML class for the problem attributes. []  
- Design the database schema for the problem attributes. []
- Design the REST API endpoints for the problem attributes. []
*/

@Entity
@Table(name = "problems")
public class Problem {
  @Id
  private Integer problemId;
  private String problemName;
  private String problemStatement; 
  private List<String> problemTags;  

  public Problem(String problemName, String problemStatement, List<String> problemTags) {
    this.problemName = problemName;
    this.problemStatement = problemStatement;
    this.problemTags = problemTags; 
  }
  public String getProblemName() {
    return problemName;
  }
  public String getProblemStatement() {
    return problemStatement;
  }
  public List<String> getProblemTags() {
    return problemTags;
  }
  public void setProblemName(String problemName) {
    this.problemName = problemName;
  }
  public void setProblemStatement(String problemStatement) {
    this.problemStatement = problemStatement; 
  }
  public void setProblemTags(List<String> problemTags) {
    this.problemTags = problemTags;
  }

}
