package com.example.cmpt276.upsolve.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_sessions")
public class StudySession {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer userID;
    private Integer problemID;

    private long durationMillis;

    private LocalDateTime timestamp;

    public StudySession() {}

    public StudySession(Integer userID, Integer problemID, long durationMillis) {
        this.userID = userID;
        this.problemID = problemID;
        this.durationMillis = durationMillis;
        this.timestamp = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }





}