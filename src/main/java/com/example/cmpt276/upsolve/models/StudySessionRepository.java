package com.example.cmpt276.upsolve.models;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Integer> {
    List<StudySession> findByUserIDAndProblemID(Integer userID, Integer problemID);
}