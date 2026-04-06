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
        regularUser = new User("user1", "pass1");
        regularUser.setUserRole("USER");

        adminUser = new User("admin", "adminpass");
        adminUser.setUserRole("ADMIN");
    }

    @Test
    public void testGetCreateCardWithUser() throws Exception {
        mockMvc.perform(get("/problems/new")
                .sessionAttr("session_user", regularUser))
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
        Mockito.when(problemRepository.findByProblemName("Problem1"))
               .thenReturn(Arrays.asList());

        mockMvc.perform(post("/problems/new")
                .sessionAttr("session_user", regularUser)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("problemName", "Problem1")
                .param("problemDescription", "Desc")
                .param("problemSolution", "Solution")
                .param("problemDifficulty", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    public void testCreateCardDuplicateProblem() throws Exception {
        Mockito.when(problemRepository.findByProblemName("Problem1"))
               .thenReturn(Arrays.asList(new Problem()));

        mockMvc.perform(post("/problems/new")
                .sessionAttr("session_user", adminUser)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("problemName", "Problem1")
                .param("problemDescription", "Desc")
                .param("problemSolution", "Solution")
                .param("problemDifficulty", "3"))
                .andExpect(status().isOk())
                .andExpect(view().name("cards/create"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    public void testCreateCardRedirectAdmin() throws Exception {
        Mockito.when(problemRepository.findByProblemName("Problem2"))
               .thenReturn(Arrays.asList());

        mockMvc.perform(post("/problems/new")
                .sessionAttr("session_user", adminUser)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("problemName", "Problem2")
                .param("problemDescription", "Desc")
                .param("problemSolution", "Solution")
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
        Problem problem = new Problem();

        regularUser.setUserID(1);
        problem.setUserID(1);

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
        Problem problem = new Problem();
        Mockito.when(problemRepository.findByProblemID(1))
               .thenReturn(Arrays.asList(problem));

        mockMvc.perform(post("/problems/update/1")
                .sessionAttr("session_user", regularUser)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("problemName", "Updated")
                .param("problemDescription", "Updated")
                .param("problemSolution", "Updated")
                .param("problemDifficulty", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/problems"));
    }

    @Test
    public void testRecordAttemptCorrect() throws Exception {
        Problem problem = new Problem();
        problem.setCorrectAttempts(0);
        problem.setIncorrectAttempts(0);

        Mockito.when(problemRepository.findByProblemID(1))
                .thenReturn(Arrays.asList(problem));

        mockMvc.perform(post("/problems/1/attempt")
                .sessionAttr("session_user", regularUser)
                .param("result", "correct"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/problems/view/1"));

        Mockito.verify(problemRepository).save(problem);
    }

    @Test
    public void testRecordAttemptIncorrect() throws Exception {
        Problem problem = new Problem();
        problem.setCorrectAttempts(0);
        problem.setIncorrectAttempts(0);

        Mockito.when(problemRepository.findByProblemID(1))
                .thenReturn(Arrays.asList(problem));

        mockMvc.perform(post("/problems/1/attempt")
                .sessionAttr("session_user", regularUser)
                .param("result", "incorrect"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/problems/view/1"));

        Mockito.verify(problemRepository).save(problem);
    }

    @Test
    public void testRecordAttemptWithoutUser() throws Exception {
        mockMvc.perform(post("/problems/1/attempt")
                .param("result", "correct"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    
}