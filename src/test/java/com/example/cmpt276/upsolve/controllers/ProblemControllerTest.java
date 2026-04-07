package com.example.cmpt276.upsolve.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import com.example.cmpt276.upsolve.models.Problem;
import com.example.cmpt276.upsolve.models.ProblemRepository;
import com.example.cmpt276.upsolve.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProblemController.class)
public class ProblemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProblemRepository problemRepository;

    private User regularUser;
    private User adminUser;

    @BeforeEach
    public void setup() {
        regularUser = new User("user1", "email@domain.com", "pass1", "What is your pet's name?", "Fluffy");
        regularUser.setUserRole("USER");

        adminUser = new User("admin", "admin@domain.com", "adminpass", "What is your favorite color?", "Blue");
        adminUser.setUserRole("ADMIN");

    }

    @Test
    public void testGetCreateCardWithUser() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("session_user", regularUser);

        mockMvc.perform(get("/problems/new").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("cards/create"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testGetCreateCardWithoutUser() throws Exception {
        mockMvc.perform(get("/problems/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testCreateCardSuccessRegularUser() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("session_user", regularUser);

        Mockito.when(problemRepository.findByProblemName("Problem1")).thenReturn(Arrays.asList());

        mockMvc.perform(post("/problems/new").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("problemName", "Problem1")
                .param("problemDescription", "Desc")
                .param("problemSolution", "Solution")
                .param("problemType", "Algo")
                .param("problemDifficulty", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    public void testCreateCardDuplicateProblem() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("session_user", adminUser);

        Mockito.when(problemRepository.findByProblemName("Problem1"))
               .thenReturn(Arrays.asList(new Problem()));

        mockMvc.perform(post("/problems/new").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("problemName", "Problem1")
                .param("problemDescription", "Desc")
                .param("problemSolution", "Solution")
                .param("problemType", "Algo")
                .param("problemDifficulty", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin_dashboard"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    public void testCreateCardRedirectAdmin() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("session_user", adminUser);

        Mockito.when(problemRepository.findByProblemName("Problem2")).thenReturn(Arrays.asList());

        mockMvc.perform(post("/problems/new").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("problemName", "Problem2")
                .param("problemDescription", "Desc")
                .param("problemSolution", "Solution")
                .param("problemType", "Algo")
                .param("problemDifficulty", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin_dashboard"));
    }

     @Test
    public void testGetProblemsWithUser() throws Exception {
        Mockito.when(problemRepository.findAll())
               .thenReturn(Arrays.asList(new Problem()));

        mockMvc.perform(get("/problems")
                .sessionAttr("session_user", regularUser))
                .andExpect(status().isOk())
                .andExpect(view().name("cards/view_all"))
                .andExpect(model().attributeExists("problems"));
    }

    @Test
    public void testGetProblemsWithoutUser() throws Exception {
        mockMvc.perform(get("/problems"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testGetViewCardSuccess() throws Exception {
        regularUser.setUserID(1);

        Problem problem = new Problem();
        problem.setProblemID(1);
        problem.setUserID(1); // IMPORTANT

        Mockito.when(problemRepository.findByProblemID(1))
           .thenReturn(Arrays.asList(problem));

        mockMvc.perform(get("/problems/view/1")
            .sessionAttr("session_user", regularUser))
            .andExpect(status().isOk())
            .andExpect(view().name("cards/study"));
    }   

    @Test
    public void testDeleteCardSuccess() throws Exception {
        Problem problem = new Problem();
        Mockito.when(problemRepository.findByProblemID(1))
               .thenReturn(Arrays.asList(problem));

        mockMvc.perform(post("/problems/delete/1")
                .sessionAttr("session_user", regularUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/problems"));
    }

    @Test
    public void testUpdateCardSuccess() throws Exception {
        regularUser.setUserID(1);

        Problem problem = new Problem();
        problem.setProblemID(1);
        problem.setUserID(1); 

        Mockito.when(problemRepository.findByProblemID(1))
           .thenReturn(Arrays.asList(problem));

        mockMvc.perform(post("/problems/update/1")
            .sessionAttr("session_user", regularUser)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("problemName", "Updated")
            .param("problemDescription", "Updated")
            .param("problemSolution", "Updated")
            .param("problemDifficulty", "1")
            .param("problemType", "Algo"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    public void testCreateCardWithTag() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("session_user", regularUser);

        Mockito.when(problemRepository.findByProblemName("TaggedProblem"))
                .thenReturn(Arrays.asList());

        mockMvc.perform(post("/problems/new").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("problemName", "TaggedProblem")
                .param("problemDescription", "Desc")
                .param("problemSolution", "Sol")
                .param("problemType", "Graph") 
                .param("problemDifficulty", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));

        Mockito.verify(problemRepository).save(Mockito.any(Problem.class)); 
    }
    
    @Test
    public void testUpdateCardTagSuccess() throws Exception {
        regularUser.setUserID(1);

        Problem problem = new Problem();
        problem.setProblemID(1);
        problem.setUserID(1);
        problem.setProblemType("DP");

        Mockito.when(problemRepository.findByProblemID(1))
            .thenReturn(Arrays.asList(problem));

        mockMvc.perform(post("/problems/update/1")
                .sessionAttr("session_user", regularUser)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("problemName", "Updated")
                .param("problemDescription", "Updated Desc")
                .param("problemSolution", "Updated Sol")
                .param("problemType", "Algo")
                .param("problemDifficulty", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));

        Mockito.verify(problemRepository).save(problem);
    }

    @Test
    public void testRecordCorrectAttempt() throws Exception {
        regularUser.setUserID(1);

        Problem problem = new Problem();
        problem.setProblemID(1);
        problem.setUserID(1);
        problem.setCorrectAttempts(2);
        problem.setIncorrectAttempts(1);

        Mockito.when(problemRepository.findByProblemID(1))
           .thenReturn(Arrays.asList(problem));

        mockMvc.perform(post("/problems/1/attempt")
            .sessionAttr("session_user", regularUser)
            .param("result", "correct"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/problems/view/1"));

        assert(problem.getCorrectAttempts() == 3);
        assert(problem.getIncorrectAttempts() == 1);

        Mockito.verify(problemRepository).save(problem);
    }

    @Test
    public void testRecordIncorrectAttempt() throws Exception {
        regularUser.setUserID(1);

        Problem problem = new Problem();
        problem.setProblemID(1);
        problem.setUserID(1);
        problem.setCorrectAttempts(2);
        problem.setIncorrectAttempts(1);

        Mockito.when(problemRepository.findByProblemID(1))
           .thenReturn(Arrays.asList(problem));

        mockMvc.perform(post("/problems/1/attempt")
            .sessionAttr("session_user", regularUser)
            .param("result", "incorrect"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/problems/view/1"));

        assert(problem.getCorrectAttempts() == 2);
        assert(problem.getIncorrectAttempts() == 2);

        Mockito.verify(problemRepository).save(problem);
    }

    @Test
    public void testRecordAttemptWithNullValues() throws Exception {
        regularUser.setUserID(1);

        Problem problem = new Problem();
        problem.setProblemID(1);
        problem.setUserID(1);
        problem.setCorrectAttempts(null);
        problem.setIncorrectAttempts(null);

        Mockito.when(problemRepository.findByProblemID(1))
           .thenReturn(Arrays.asList(problem));

        mockMvc.perform(post("/problems/1/attempt")
            .sessionAttr("session_user", regularUser)
            .param("result", "correct"))
            .andExpect(status().is3xxRedirection());

        assert(problem.getCorrectAttempts() == 1);
        assert(problem.getIncorrectAttempts() == 0);
    }

    @Test
    public void testRecordAttemptInvalidResult() throws Exception {
        regularUser.setUserID(1);

        Problem problem = new Problem();
        problem.setProblemID(1);
        problem.setUserID(1);
        problem.setCorrectAttempts(1);
        problem.setIncorrectAttempts(1);

        Mockito.when(problemRepository.findByProblemID(1))
           .thenReturn(Arrays.asList(problem));

        mockMvc.perform(post("/problems/1/attempt")
            .sessionAttr("session_user", regularUser)
            .param("result", "invalid"))
            .andExpect(status().is3xxRedirection());

        assert(problem.getCorrectAttempts() == 1);
        assert(problem.getIncorrectAttempts() == 1);
    }

    @Test
    public void testRecordAttemptNoUser() throws Exception {
        mockMvc.perform(post("/problems/1/attempt")
            .param("result", "correct"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testAccuracyCalculationNormal() throws Exception {
        regularUser.setUserID(1);

        Problem problem = new Problem();
        problem.setProblemID(1);
        problem.setUserID(1);
        problem.setCorrectAttempts(3);
        problem.setIncorrectAttempts(1);

        Mockito.when(problemRepository.findByUserID(1))
           .thenReturn(Arrays.asList(problem));

        mockMvc.perform(get("/problems")
            .sessionAttr("session_user", regularUser))
            .andExpect(status().isOk())
            .andExpect(view().name("cards/view_all"))
            .andExpect(model().attributeExists("problems"));

        assert(problem.getAccuracy() == 75.0);
    }

    @Test
    public void testAccuracyZeroAttempts() throws Exception {
        regularUser.setUserID(1);

        Problem problem = new Problem();
        problem.setProblemID(1);
        problem.setUserID(1);
        problem.setCorrectAttempts(0);
        problem.setIncorrectAttempts(0);

        Mockito.when(problemRepository.findByUserID(1))
           .thenReturn(Arrays.asList(problem));

        mockMvc.perform(get("/problems")
            .sessionAttr("session_user", regularUser))
            .andExpect(status().isOk());

        assert(problem.getAccuracy() == -1);
    } 

    @Test
    public void testAccuracyWithNullAttempts() throws Exception {
        regularUser.setUserID(1);

        Problem problem = new Problem();
        problem.setProblemID(1);
        problem.setUserID(1);
        problem.setCorrectAttempts(null);
        problem.setIncorrectAttempts(null);

        Mockito.when(problemRepository.findByUserID(1))
           .thenReturn(Arrays.asList(problem));

        mockMvc.perform(get("/problems")
            .sessionAttr("session_user", regularUser))
            .andExpect(status().isOk());

        assert(problem.getAccuracy() == -1);
    }

    @Test
    public void testAccuracyMultipleProblems() throws Exception {
        regularUser.setUserID(1);

        Problem p1 = new Problem();
        p1.setUserID(1);
        p1.setCorrectAttempts(2);
        p1.setIncorrectAttempts(2); 

        Problem p2 = new Problem();
        p2.setUserID(1);
        p2.setCorrectAttempts(1);
        p2.setIncorrectAttempts(0);

        Mockito.when(problemRepository.findByUserID(1))
           .thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/problems")
            .sessionAttr("session_user", regularUser))
            .andExpect(status().isOk());

        assert(p1.getAccuracy() == 50.0);
        assert(p2.getAccuracy() == 100.0);
    }

    @Test
    public void testAccuracyAdminUsesFindAll() throws Exception {
        adminUser.setUserID(99);

        Problem problem = new Problem();
        problem.setCorrectAttempts(4);
        problem.setIncorrectAttempts(1); 

        Mockito.when(problemRepository.findAll())
           .thenReturn(Arrays.asList(problem));

        mockMvc.perform(get("/problems")
            .sessionAttr("session_user", adminUser))
            .andExpect(status().isOk());

        assert(problem.getAccuracy() == 80.0);
    }

    
}

