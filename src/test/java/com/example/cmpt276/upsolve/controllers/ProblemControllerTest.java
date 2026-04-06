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

        mockMvc.perform(get("/create_card").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("create_card"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testGetCreateCardWithoutUser() throws Exception {
        mockMvc.perform(get("/create_card"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testCreateCardSuccessRegularUser() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("session_user", regularUser);

        Mockito.when(problemRepository.findByProblemName("Problem1")).thenReturn(Arrays.asList());

        mockMvc.perform(post("/create_card").session(session)
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                .param("problemName", "Problem1")
                .param("problemDescription", "Desc")
                .param("problemSolution", "Solution")
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

        mockMvc.perform(post("/create_card").session(session)
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                .param("problemName", "Problem1")
                .param("problemDescription", "Desc")
                .param("problemSolution", "Solution")
                .param("problemDifficulty", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/create_card"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    public void testCreateCardRedirectAdmin() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("session_user", adminUser);

        Mockito.when(problemRepository.findByProblemName("Problem2")).thenReturn(Arrays.asList());

        mockMvc.perform(post("/create_card").session(session)
                .contentType(MediaType.valueOf("application/x-www-form-urlencoded"))
                .param("problemName", "Problem2")
                .param("problemDescription", "Desc")
                .param("problemSolution", "Solution")
                .param("problemDifficulty", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin_dashboard"));
    }
}