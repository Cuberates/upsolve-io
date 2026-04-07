package com.example.cmpt276.upsolve.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Table(name="problems")
@Entity
public class Problem {
    @Id 
    @GeneratedValue
    Integer problemID;
    Integer userID;
    String problemName; 
    String problemDescription;
    String problemSolution;
    String problemType;
    int problemDifficulty;
    boolean studied = false;
    @Column(name="correctAttempts")
    Integer correctAttempts;
    @Column(name="incorrectAttempts")
    Integer incorrectAttempts;

    @Transient
    double accuracy;
    
    @Transient
    long totalTimeMillis;

    public Problem() {}
    public Problem(String problemName, String problemDescription, String problemSolution, int problemDifficulty, String problemType) {
        this.problemName = problemName;
        this.problemDescription = problemDescription;
        this.problemSolution = problemSolution;
        this.problemDifficulty = problemDifficulty;
        this.problemType = problemType;
    }
    public Integer getUserID() {
        return userID;
    }
    public void setUserID(Integer userID) {
        this.userID = userID;
    }
    public Integer getProblemID() {
        return problemID;
    }
    public void setProblemID(Integer problemID) {
        this.problemID = problemID;
    }
    public String getProblemName() {
        return problemName;
    }
    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }
    public String getProblemDescription() {
        return problemDescription;
    }
    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }
    public String getProblemSolution() {
        return problemSolution;
    }
    public void setProblemSolution(String problemSolution) {
        this.problemSolution = problemSolution;
    }
    public int getProblemDifficulty() {
        return problemDifficulty;
    }
    public void setProblemDifficulty(int problemDifficulty) {
        this.problemDifficulty = problemDifficulty;
    }
    
    public boolean isStudied() {
        return studied;
    }
    public void setStudied(boolean studied) {
        this.studied = studied;
    }
    public String getProblemType() {
    return problemType;
    }
    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }
    public Integer getCorrectAttempts() {
        return correctAttempts != null ? correctAttempts : 0;
    }
    public void setCorrectAttempts(Integer correctAttempts) {
        this.correctAttempts = correctAttempts;
    }
    public Integer getIncorrectAttempts() {
        return incorrectAttempts != null ? incorrectAttempts : 0;
    }
    public void setIncorrectAttempts(Integer incorrectAttempts) {
        this.incorrectAttempts = incorrectAttempts;
    }
    public double getAccuracy() {
        return accuracy;
    }
    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
    public long getTotalTimeMillis() {
        return totalTimeMillis;
    }
    public void setTotalTimeMillis(long totalTimeMillis) {
        this.totalTimeMillis = totalTimeMillis;
    }
}
