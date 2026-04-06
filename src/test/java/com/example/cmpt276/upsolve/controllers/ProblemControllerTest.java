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
        regularUser = new User("user1", "pass1");
        regularUser.setUserRole("USER");

        adminUser = new User("admin", "adminpass");
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
                .andExpect(status().isOk())
                .andExpect(view().name("cards/create"))
                .andExpect(model().attributeExists("errorMessage"));
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

        Mockito.verify(problemRepository).save(Mockito.any(Problem.class)); // ✅ important
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


}